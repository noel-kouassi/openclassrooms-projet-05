import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, waitForAsync} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';

import {ListComponent} from './list.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {Router} from "@angular/router";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {RouterTestingModule} from "@angular/router/testing";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let routerMock: jest.Mocked<Router>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionList = [{id: 1}] as Session[];

  beforeEach(async () => {
    sessionServiceMock = mockSessionService as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      all: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.all>,
    } as jest.Mocked<SessionApiService>

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>
    } as jest.Mocked<Router>;

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        {provide: SessionService, useValue: sessionServiceMock},
        {provide: SessionApiService, useValue: sessionApiServiceMock}
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get user data', () => {
    expect(component.user).toEqual(sessionServiceMock.sessionInformation);
  });

  it('should get all sessions on start', waitForAsync(() => {

    // When
    sessionApiServiceMock.all.mockReturnValue(of(mockSessionList));
    fixture.detectChanges();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.all).toHaveBeenCalled();
    });
  }));

  it('should show create button for admin users', () => {

    // When
    fixture.detectChanges();
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');

    // Then
    expect(createButton).toBeTruthy();
  });

  it('should not show create button for non-admin users', () => {

    // Given
    sessionServiceMock.sessionInformation = {
      admin: false
    } as SessionInformation;

    // When
    fixture.detectChanges();
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');

    // Then
    expect(createButton).toBeFalsy();
  });

});
