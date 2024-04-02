import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {FormComponent} from './form.component';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SessionApiService} from '../../services/session-api.service';
import {of} from 'rxjs';
import {Session} from '../../interfaces/session.interface';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {SessionService} from "../../../../services/session.service";
import {RouterTestingModule} from "@angular/router/testing";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule} from "@angular/common";
import {expect} from "@jest/globals";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;
  let routerMock: jest.Mocked<Router>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  const mockSession = {
    name: 'test',
    description: 'test',
    date: new Date(),
    teacher_id: 1,
    users: [1]
  } as Session;

  const mockSessionForm = {
    value: mockSession
  }

  beforeEach(async () => {
    activatedRouteMock = {
      snapshot: {
        // @ts-ignore
        paramMap: {
          get: () => '1'
        }
      },
      // @ts-ignore
      url: of([{path: 'update'}])
    };

    routerMock = {
      navigate: jest.fn(),
      url: '/sessions/update/1'
    } as any as jest.Mocked<Router>;

    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of({} as Session)),
      update: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.update>,
      create: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.create>,
    } as any as jest.Mocked<SessionApiService>;

    matSnackBarMock = {
      open: jest.fn()
    } as any as jest.Mocked<MatSnackBar>;

    sessionServiceMock = {
      sessionInformation: {admin: true},
    } as jest.Mocked<SessionService>;

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        CommonModule,
        FormsModule],
      declarations: [FormComponent],
      providers: [
        FormBuilder,
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: Router, useValue: routerMock},
        {provide: SessionApiService, useValue: sessionApiServiceMock},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: SessionService, useValue: sessionServiceMock},
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to sessions page if user is not admin on initialization', () => {

    // Given
    sessionServiceMock.sessionInformation = {admin: false} as SessionInformation;

    // When
    component.ngOnInit();

    // Then
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onUpdate to true when url includes update', () => {

    // When
    component.ngOnInit();

    // Then
    expect(component.onUpdate).toBe(true);
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
  });

  it('should not navigate to sessions page if user is admin on initialization', () => {

    // Given
    sessionServiceMock.sessionInformation = {admin: true} as SessionInformation;

    // When
    component.ngOnInit();

    // Then
    // @ts-ignore
    expect(routerMock.navigate(['/sessions'])).toBeFalsy();
  });

  it('should create session and exit page on creation mode', fakeAsync(() => {

    // Given
    component.sessionForm = mockSessionForm as FormGroup;
    component.onUpdate = false;

    // When
    // @ts-ignore
    sessionApiServiceMock.create.mockReturnValue(of(mockSession));

    component.submit();
    tick();

    // Then
    expect(sessionApiServiceMock.create).toHaveBeenCalledWith(component.sessionForm?.value);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', {duration: 3000});
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should update session and exit page on update mode', fakeAsync(() => {

    // Given
    component.onUpdate = true;
    component.id = '1';
    component.sessionForm = mockSessionForm as FormGroup;
    sessionServiceMock.sessionInformation = {admin: true} as SessionInformation;

    // When
    sessionApiServiceMock.update.mockReturnValue(of(mockSession));

    component.submit();
    tick();

    // Then
    expect(sessionApiServiceMock.update).toHaveBeenCalledWith('1', component.sessionForm?.value);
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', {duration: 3000});
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should display create session title when not updating', () => {

    // Given
    const compiled = fixture.nativeElement;
    component.onUpdate = false;

    // When
    fixture.detectChanges();

    // Then
    expect(compiled.querySelector('h1').textContent).toContain('Create session');
  });

  it('should display update session title when updating', () => {

    // Given
    const compiled = fixture.nativeElement;
    component.onUpdate = true;

    // When
    fixture.detectChanges();

    // Then
    expect(compiled.querySelector('h1').textContent).toContain('Update session');
  });
});
