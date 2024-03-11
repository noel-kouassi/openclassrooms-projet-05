import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {MatToolbarModule} from '@angular/material/toolbar';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';

import {AppComponent} from './app.component';
import {Router} from "@angular/router";
import {SessionService} from "./services/session.service";
import {of} from "rxjs";
import {By} from "@angular/platform-browser";

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let routerMock: jest.Mocked<Router>;
  let sessionServiceMock: jest.Mocked<SessionService>;

  beforeEach(async () => {

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>
    } as jest.Mocked<Router>;

    sessionServiceMock = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      $isLogged: jest.fn() as jest.MockedFunction<typeof sessionServiceMock.$isLogged>,
      logOut: jest.fn() as jest.MockedFunction<typeof sessionServiceMock.logOut>
    } as jest.Mocked<SessionService>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule,
        RouterTestingModule.withRoutes([])
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        {provide: Router, useValue: routerMock},
        {provide: SessionService, useValue: sessionServiceMock}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should return user connection on log in', () => {

    // Given
    const isLoggedMockValue = true;

    // When
    sessionServiceMock.$isLogged.mockReturnValue(of(isLoggedMockValue));

    // Then
    component.$isLogged().subscribe((result) => {
      expect(result).toEqual(isLoggedMockValue);
      expect(sessionServiceMock.$isLogged).toHaveBeenCalled();
    });
  });

  it('should log out and navigate to home', () => {

    // When
    component.logout();

    // Then
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });

  it('should display the app title', () => {

    // When
    const titleElement = fixture.debugElement.query(By.css('span')).nativeElement;

    // Then
    expect(titleElement.textContent).toContain('Yoga app');
  });
});
