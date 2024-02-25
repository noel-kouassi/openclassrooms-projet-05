import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionApiService} from '../../services/session-api.service';

import {FormComponent} from './form.component';
import {ActivatedRoute, Router} from "@angular/router";
import {TeacherService} from "../../../../services/teacher.service";
import {of} from "rxjs";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {Session} from "../../interfaces/session.interface";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;
  let formBuilder: FormBuilder;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let routerMock: jest.Mocked<Router>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSession = {
    name: 'test',
    description: 'test',
    date: new Date(),
    teacher_id: 1,
    users: [1]
  } as Session;

  const mockSessionForm = {
    value : mockSession
  }

  beforeEach(async () => {

    activatedRouteMock = {
      snapshot: {
            paramMap: {
              get: jest.fn()
            }
      },
      url: of([]),
    } as any as jest.Mocked<ActivatedRoute>;

    formBuilder = new FormBuilder();

    matSnackBarMock = {
      open: jest.fn() as jest.MockedFunction<typeof matSnackBarMock.open>,
    } as jest.Mocked<MatSnackBar>;

    sessionApiServiceMock = {
      create: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.create>,
      update: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.update>,
      detail: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.detail>,
    } as jest.Mocked<SessionApiService>;

    sessionServiceMock = {
      sessionInformation: {admin: true},
    } as jest.Mocked<SessionService>;

    teacherServiceMock = {
      all: jest.fn() as jest.MockedFunction<typeof teacherServiceMock.all>,
    } as jest.Mocked<TeacherService>;

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>,
      url: ''
    } as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        SessionApiService,
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: FormBuilder, useValue: formBuilder},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: SessionApiService, useValue: sessionApiServiceMock},
        {provide: SessionService, useValue: sessionServiceMock},
        {provide: TeacherService, useValue: teacherServiceMock},
        {provide: Router, useValue: routerMock}
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to sessions page if user is not admin on initialization', () => {

    // Given
    sessionServiceMock.sessionInformation = { admin: false } as SessionInformation;

    // When
    component.ngOnInit();

    // Then
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should not navigate to sessions page if user is admin on initialization', () => {

    // Given
    sessionServiceMock.sessionInformation = { admin: true } as SessionInformation;

    // When
    component.ngOnInit();

    // Then
    expect(routerMock.navigate(['/sessions'])).toBeFalsy();
  });

  it('should create session and exit page on creation mode', waitForAsync(() => {

    // Given
    component.sessionForm = mockSessionForm as FormGroup;

    // When
    sessionApiServiceMock.create.mockReturnValue(of(mockSession));

    component.submit();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.create).toHaveBeenCalledWith(component.sessionForm?.value);
      expect(matSnackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  }));

  it('should update session and exit page on update mode', waitForAsync(() => {

    // Given
    component.onUpdate = true;
    component.id = '1';
    component.sessionForm = mockSessionForm as FormGroup;

    // When
    sessionApiServiceMock.update.mockReturnValue(of(mockSession));

    component.submit();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.update).toHaveBeenCalledWith('1', component.sessionForm?.value);
      expect(matSnackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  }));
});
