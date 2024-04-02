import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {DetailComponent} from './detail.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {RouterTestingModule} from '@angular/router/testing';
import {ActivatedRoute, Router} from '@angular/router';
import {SessionApiService} from '../../services/session-api.service';
import {of} from 'rxjs';
import {Session} from '../../interfaces/session.interface';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {expect} from "@jest/globals";
import {SessionService} from "../../../../services/session.service";
import {TeacherService} from "../../../../services/teacher.service";
import {Teacher} from "../../../../interfaces/teacher.interface";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiServiceMock: any;
  let matSnackBarMock: any;
  let routerMock: any;
  let activatedRouteMock: any;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let teacherServiceMock: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  beforeEach(async () => {
    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of("")),
      delete: jest.fn().mockReturnValue(of("")), // Mocking delete method to return observable
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    sessionServiceMock = mockSessionService as jest.Mocked<SessionService>;

    matSnackBarMock = {
      open: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {get: jest.fn().mockReturnValue('1')}
      },
      url: of([])
    };

    teacherServiceMock = {
      detail: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientTestingModule
      ],
      declarations: [DetailComponent],
      providers: [
        {provide: SessionApiService, useValue: sessionApiServiceMock},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: Router, useValue: routerMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: SessionService, useValue: sessionServiceMock},
        {provide: TeacherService, useValue: teacherServiceMock}
      ]
    }).compileComponents();

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

  it('should delete session and navigate to sessions', fakeAsync(() => {
    // Given
    const deleteResponse = of({});
    sessionApiServiceMock.delete.mockReturnValue(deleteResponse);

    // When
    component.delete();
    tick();

    // Then
    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', {duration: 3000});
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  }));

  it('should participate to the session', fakeAsync(() => {

    // Given
    component.sessionId = '1';
    component.userId = '1';

    let mockSession = {
      name: 'Test Mock',
      users: [2, 3],
      teacher_id: 1
    } as Session;

    let mockTeacher = {
      id: 1,
      firstName: 'Teacher',
      lastName: 'Test',
    } as Teacher;

    sessionApiServiceMock.detail.mockReturnValue(of(mockSession));
    sessionApiServiceMock.participate.mockReturnValue(of({}));
    teacherServiceMock.detail.mockReturnValue(of(mockTeacher));

    // When
    component.participate();
    tick();

    // Then
    expect(sessionApiServiceMock.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
  }));

  it('should unParticipate to the session', fakeAsync(() => {

    // Given
    component.sessionId = '1';
    component.userId = '1';

    let mockSession = {
      name: 'Test Mock',
      users: [1],
      teacher_id: 1
    } as Session;

    let mockTeacher = {
      id: 1,
      firstName: 'Teacher',
      lastName: 'Test',
    } as Teacher;

    sessionApiServiceMock.detail.mockReturnValue(of(mockSession));
    sessionApiServiceMock.unParticipate.mockReturnValue(of({}));
    teacherServiceMock.detail.mockReturnValue(of(mockTeacher));

    // When
    component.unParticipate();
    tick();

    // Then
    expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
  }));

  it('should display session details for admin', () => {

    // Given

    // @ts-ignore
    component.session = {
      name: 'Test Mock',
      users: []
    } as Session;

    component.isAdmin = true;

    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    // Then
    expect(compiled.querySelector('h1').textContent).toContain('Test Mock');
    expect(compiled.querySelector('button[mat-raised-button][color="warn"]').textContent).toContain('Delete');
  });

  it('should display session details for non-admin', () => {

    // Given

    // @ts-ignore
    component.session = {
      name: 'Test Mock',
      users: []
    } as Session;

    component.isAdmin = false;

    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    // Then
    expect(compiled.querySelector('h1').textContent).toContain('Test Mock');
    expect(compiled.querySelector('button[mat-raised-button][color="primary"]').textContent).toContain('Participate');
  });
});
