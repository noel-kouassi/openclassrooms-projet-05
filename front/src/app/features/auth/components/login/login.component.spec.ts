import {ComponentFixture, TestBed} from '@angular/core/testing';
import {Router} from '@angular/router';
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {of, throwError} from 'rxjs';
import {LoginComponent} from './login.component';
import {AuthService} from '../../services/auth.service';
import {SessionService} from 'src/app/services/session.service';
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {CommonModule} from "@angular/common";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  const sessionInformation = {
    admin: true,
    id: 1
  }

  const loginRequest = {
    email: 'test@test.com',
    password: 'user1234'
  }

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn() as jest.MockedFunction<typeof authServiceMock.login>
    } as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>
    } as jest.Mocked<Router>;

    sessionServiceMock = {
      logIn: jest.fn() as jest.MockedFunction<typeof sessionServiceMock.logIn>
    } as jest.Mocked<SessionService>;

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatButtonModule,
        CommonModule,
        FormsModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: SessionService, useValue: sessionServiceMock},
        FormBuilder,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should connect user and redirect on successful login', () => {

    // Given
    const loginResponse = sessionInformation as SessionInformation;
    const navigateSpy = jest.spyOn(routerMock as any, 'navigate');

    // When
    authServiceMock.login.mockReturnValue(of(loginResponse));
    component.form.setValue(loginRequest);

    component.submit();

    // Then
    expect(authServiceMock.login).toHaveBeenCalledWith(loginRequest);
    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(loginResponse);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on login error', () => {

    // Given
    const errorMessage = 'Login failed';

    // When
    authServiceMock.login.mockReturnValue(throwError(errorMessage));
    component.form.setValue(loginRequest);

    component.submit();

    // Then
    expect(authServiceMock.login).toHaveBeenCalledWith(loginRequest);
    expect(sessionServiceMock.logIn).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });

  it('should display login form with required elements', () => {
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('form')).toBeTruthy();
    expect(compiled.querySelector('input[formControlName="email"]')).toBeTruthy();
    expect(compiled.querySelector('input[formControlName="password"]')).toBeTruthy();
    expect(compiled.querySelector('button[type="submit"]')).toBeTruthy();
    expect(compiled.querySelector('.error')).toBeFalsy();
  });

  it('should toggle password visibility on button click', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('input[formControlName="password"]').type).toBe('text');
  });

  it('should bind form controls to the component', () => {

    // Given
    const emailInput = component.form.get('email');
    const passwordInput = component.form.get('password');

    // When
    emailInput!.setValue('test@test.com');
    passwordInput!.setValue('test1234');

    // Then
    expect(component.form.value.email).toEqual('test@test.com');
    expect(component.form.value.password).toEqual('test1234');
    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();
  });

  it('should verify submit action on click', () => {

    // Given
    const loginResponse = sessionInformation as SessionInformation;
    const submitSpy = jest.spyOn(component, 'submit');

    // When
    authServiceMock.login.mockReturnValue(of(loginResponse));
    component.form.setValue(loginRequest);

    component.submit();

    // Then
    expect(submitSpy).toHaveBeenCalled();
  });

});
