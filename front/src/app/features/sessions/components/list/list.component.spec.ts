import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';

import {ListComponent} from './list.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSessionList = [{id: 1}] as Session[];

  beforeEach(async () => {
    sessionServiceMock = mockSessionService as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      all: jest.fn() as jest.MockedFunction<typeof sessionApiServiceMock.all>,
    } as jest.Mocked<SessionApiService>

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{provide: SessionService, useValue: mockSessionService},
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

  it('should get all sessions on start', waitForAsync(() => {

    // When
    sessionApiServiceMock.all.mockReturnValue(of(mockSessionList));
    fixture.detectChanges();

    // Then
    fixture.whenStable().then(() => {
      expect(sessionApiServiceMock.all).toHaveBeenCalled();
    });
  }));

  it('should get user data', () => {
    expect(component.user).toEqual(sessionServiceMock.sessionInformation);
  });
});
