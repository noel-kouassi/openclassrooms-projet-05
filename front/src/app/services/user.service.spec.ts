import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {UserService} from './user.service';
import {User} from '../interfaces/user.interface';

describe('UserService', () => {
  let userService: UserService;
  let httpTestingController: HttpTestingController;
  let pathService: string;

  beforeEach(() => {
    pathService = 'api/user';

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    userService = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('should get user by id', () => {

    // Given
    const userId = '1';
    const mockUser: User = {
      id: 1,
      email: 'test@test.com',
      lastName: 'test',
      firstName: 'test',
      admin: false,
      password: 'test1234',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    // When
    userService.getById(userId).subscribe((user) => {
      expect(user).toEqual(mockUser);
    });
    const req = httpTestingController.expectOne(`${pathService}/${userId}`);

    // Then
    expect(req.request.method).toEqual('GET');
    req.flush(mockUser);
  });

  it('should delete user by id', () => {

    // Given
    const userId = '1';

    // When
    userService.delete(userId).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/${userId}`);

    // Then
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });
});
