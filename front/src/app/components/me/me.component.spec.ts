import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
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
import {By} from "@angular/platform-browser";

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
      delete: jest.fn(() => of(true)),
      getById: jest.fn(() => of({firstName: 'Alex', lastName: 'Robert', email: 'alex.robert@test.com'} as User))
    } as any as jest.Mocked<UserService>;

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

  it('should print user information in the template', waitForAsync(() => {

    // When
    fixture.detectChanges();

    // Then
    fixture.whenStable().then(() => {
      fixture.detectChanges();

      const nameElement = fixture.debugElement.query(By.css('p:first-child'));
      expect(nameElement.nativeElement.textContent).toContain('Alex ROBERT');

      const emailElement = fixture.debugElement.query(By.css('p:nth-child(2)'));
      expect(emailElement.nativeElement.textContent).toContain('alex.robert@test.com');
    });
  }));

  it('should print admin message when user is admin', waitForAsync(() => {

    // When
    userServiceMock.getById.mockReturnValue(of({
      firstName: 'Admin',
      lastName: 'User',
      email: 'admin@test.com',
      admin: true
    } as User));

    fixture.detectChanges();

    // Then
    fixture.whenStable().then(() => {
      fixture.detectChanges();

      const adminMessageElement = fixture.debugElement.query(By.css('.my2'));
      expect(adminMessageElement.nativeElement.textContent).toContain('You are admin');
    });
  }));

  it('should print delete button when user is not admin', waitForAsync(() => {

    // When
    userServiceMock.getById.mockReturnValue(of({
      firstName: 'Alex',
      lastName: 'Robert',
      email: 'alex.robert@test.com',
      admin: false
    } as User));

    fixture.detectChanges();

    // Then
    fixture.whenStable().then(() => {
      fixture.detectChanges();

      const deleteButton = fixture.debugElement.query(By.css('button[mat-raised-button]'));
      expect(deleteButton).toBeTruthy();
    });
  }));
});
