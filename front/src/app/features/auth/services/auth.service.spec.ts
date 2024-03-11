import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {AuthService} from './auth.service';
import {RegisterRequest} from '../interfaces/registerRequest.interface';
import {LoginRequest} from '../interfaces/loginRequest.interface';
import {SessionInformation} from 'src/app/interfaces/sessionInformation.interface';
import {of} from "rxjs";

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;

  const sessionInformation = {
    admin: true,
    id: 1
  } as SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  it('should send a post request to register', () => {

    // Given
    const registerRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'test',
      lastName: 'test',
      password: 'test1234'
    };
    const pathService = 'api/auth';

    // When
    authService.register(registerRequest).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/register`);

    // Then
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(registerRequest);
    req.flush(null);
  });

  it('should send a post request to login', () => {

    // Given
    const loginRequest: LoginRequest = {
      email: 'test@test.com',
      password: 'test1234'
    };
    const pathService = 'api/auth';

    // When
    authService.login(loginRequest).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/login`);

    // Then
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(loginRequest);
    req.flush(of(sessionInformation));
  });

});
