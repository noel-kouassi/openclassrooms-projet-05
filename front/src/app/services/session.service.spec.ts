import {TestBed} from '@angular/core/testing';
import {SessionService} from './session.service';
import {SessionInformation} from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;
  const mockSessionInformation = {
    admin: true,
    id: 1
  } as SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });

    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should start with isLogged as false', () => {

    // When
    sessionService.$isLogged().subscribe((isLogged) => {

      // Then
      expect(isLogged).toBeFalsy();
    });
  });

  it('should log in and update isLogged', () => {

    // When
    sessionService.logIn(mockSessionInformation);

    sessionService.$isLogged().subscribe((isLogged) => {

      // Then
      expect(isLogged).toBeTruthy();
    });
  });

  it('should log out and update isLogged', () => {

    // When
    sessionService.logIn(mockSessionInformation);

    sessionService.logOut();

    sessionService.$isLogged().subscribe((isLogged) => {

      // Then
      expect(isLogged).toBeFalsy();
    });
  });

  it('should update sessionInformation when logged in', () => {

    // When
    sessionService.logIn(mockSessionInformation);

    // Then
    expect(sessionService.sessionInformation).toEqual(mockSessionInformation);
  });

  it('should clear sessionInformation when logged out', () => {

    // When
    sessionService.logIn(mockSessionInformation);

    sessionService.logOut();

    // Then
    expect(sessionService.sessionInformation).toBeUndefined();
  });
});
