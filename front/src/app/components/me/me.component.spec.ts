import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
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
  let fixture: ComponentFixture<MeComponent>;
  let userServiceMock: jest.Mocked<UserService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async () => {
    userServiceMock = {
      delete: jest.fn() as jest.MockedFunction<typeof userServiceMock.delete>,
      getById: jest.fn() as jest.MockedFunction<typeof userServiceMock.getById>
    } as jest.Mocked<UserService>;

    sessionServiceMock = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn() as jest.MockedFunction<typeof sessionServiceMock.logOut>
    } as jest.Mocked<SessionService>;

    matSnackBarMock = {
      open: jest.fn() as jest.MockedFunction<typeof matSnackBarMock.open>
    } as jest.Mocked<MatSnackBar>;

    routerMock = {
      navigate: jest.fn() as jest.MockedFunction<typeof routerMock.navigate>
    } as jest.Mocked<Router>;

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
      providers: [
        {provide: SessionService, useValue: sessionServiceMock},
        {provide: UserService, useValue: userServiceMock},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: Router, useValue: routerMock}
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should retrieve user on page loading with ngOnInit', () => {

    // Given
    let mockUser: User = {id: 1} as User;

    // When
    userServiceMock.getById.mockReturnValue(of(mockUser));

    component.ngOnInit();

    // Then
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');

    setTimeout(() => {
      expect(component.user).toEqual(mockUser);
    }, 1000);

  });

  it('should go back in navigation on back function execution', () => {

    // Given
    let mockHistoryBack = jest.spyOn(window.history as any, 'back');

    // When
    component.back();

    // Then
    expect(mockHistoryBack).toHaveBeenCalled();
  });

  it('should delete user account, logout and make redirection on removal action', () => {

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
