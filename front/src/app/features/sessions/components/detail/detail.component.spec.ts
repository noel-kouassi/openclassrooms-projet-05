import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import {ActivatedRoute, Router} from "@angular/router";
import {SessionApiService} from "../../services/session-api.service";
import {TeacherService} from "../../../../services/teacher.service";
import {of} from "rxjs";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let routeMock: jest.Mocked<ActivatedRoute>;
  let formBuilder: FormBuilder;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let routerMock: jest.Mocked<Router>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    routeMock = {
      snapshot: {
        paramMap: { get: jest.fn() }
      },
      url: of([]),
    } as any as jest.Mocked<ActivatedRoute>;

    formBuilder = new FormBuilder();

    matSnackBarMock = {
      open: jest.fn() as jest.MockedFunction<typeof matSnackBarMock.open>
    } as jest.Mocked<MatSnackBar>;

    sessionApiServiceMock = {
      delete: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.delete>,
      participate: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.participate>,
      unParticipate: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.unParticipate>,
      detail: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.detail>
    } as jest.Mocked<SessionApiService>;

    sessionServiceMock = mockSessionService as jest.Mocked<SessionService>;

    teacherServiceMock = {
      detail: jest.fn() as jest.MockedFunction<typeof teacherServiceMock.detail>
    } as jest.Mocked<TeacherService>;

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>,
    } as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        SessionApiService,
        { provide: ActivatedRoute, useValue: routeMock },
        { provide: FormBuilder, useValue: formBuilder },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: Router, useValue: routerMock }
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should go back on previous function execution', () => {

    // Given
    let mockHistoryBack = jest.spyOn(window.history as any, 'back');

    // When
    component.back();

    // Then
    expect(mockHistoryBack).toHaveBeenCalled();
  });

  it('should delete session and navigate to sessions', waitForAsync(() => {

    // Given
    component.sessionId = '1';

    // When
    component.delete();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.delete).toHaveBeenCalledWith(component.sessionId);
      expect(matSnackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
    });
  }));

  it('should participate to the session', waitForAsync(() => {

    // Given
    component.sessionId = '1';
    component.userId = '1';

    // When
    component.participate();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  }));

  it('should not participate to the session', waitForAsync(() => {

    // Given
    component.sessionId = '1';
    component.userId = '1';

    // When
    component.unParticipate();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  }));

});

