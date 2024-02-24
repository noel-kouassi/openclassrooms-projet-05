import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@jest/globals';

import {RegisterComponent} from './register.component';
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";
import {RegisterRequest} from "../../interfaces/registerRequest.interface";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: jest.Mocked<AuthService>;

  const registerRequest = {
    email: 'test@test.com',
    firstName: 'Test',
    lastName: 'Test',
    password: 'password1234'
  } as RegisterRequest;

  beforeEach(async () => {
    authServiceMock = {
      register: jest.fn() as jest.MockedFunction<typeof authServiceMock.register>
    } as jest.Mocked<AuthService>;

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        FormBuilder
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form and redirect on successful registration', () => {

    // Given
    authServiceMock.register.mockReturnValue(of<void>());

    // When
    component.form.setValue(registerRequest);
    component.submit();

    // Then
    expect(authServiceMock.register).toHaveBeenCalledWith(registerRequest);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on registration error', () => {

    // Given
    const errorMessage = 'Registration failed';

    // When
    authServiceMock.register.mockReturnValue(throwError(errorMessage));
    component.form.setValue(registerRequest);

    component.submit();

    // Then
    expect(authServiceMock.register).toHaveBeenCalledWith(registerRequest);
    expect(component.onError).toBeTruthy();
  });
});
