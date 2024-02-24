import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {SessionService} from 'src/app/services/session.service';

import {MeComponent} from './me.component';
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {of} from "rxjs";
import {User} from "../../interfaces/user.interface";

jest.mock('../../services/user.service');

describe('MeComponent', () => {
  let component: MeComponent;
  let userServiceMock: jest.Mocked<UserService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{provide: SessionService, useValue: mockSessionService}],
    })
      .compileComponents();

    userServiceMock = {} as jest.Mocked<UserService>;
    userServiceMock.delete = jest.fn();
    userServiceMock.getById = jest.fn();

    matSnackBarMock = {} as jest.Mocked<MatSnackBar>;
    matSnackBarMock.open = jest.fn();

    sessionServiceMock = mockSessionService as jest.Mocked<SessionService>;
    sessionServiceMock.logOut = jest.fn();

    routerMock = {} as jest.Mocked<Router>;
    routerMock.navigate = jest.fn();

    component = new MeComponent(routerMock, sessionServiceMock, matSnackBarMock, userServiceMock);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should retrieve user when loading page with ngOnInit', () => {

    // Given
    let mockUser: User = {} as User;
    mockUser.id = 1;

    // When
    userServiceMock.getById.mockReturnValue(of(mockUser));

    component.ngOnInit();

    // Then
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');

    setTimeout(() => {
      expect(component.user).toEqual(mockUser);
    }, 1000);

  });

  it('should go back in navigation when execute back function', () => {

    // Given
    let mockHistoryBack = jest.spyOn(window.history as any, 'back');

    // When
    component.back();

    // Then
    expect(mockHistoryBack).toHaveBeenCalled();
  });

  it('should delete user account, logout and make redirection after removal', () => {

    // Given
    userServiceMock.delete.mockReturnValue(of({}));

    // When
    component.delete();

    // Then
    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', {duration: 3000});
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});
