import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {TeacherService} from './teacher.service';
import {Teacher} from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let teacherService: TeacherService;
  let httpTestingController: HttpTestingController;
  let pathService: string;
  const mockTeacher = {
    id: 1,
    lastName: 'test',
    firstName: 'test',
    createdAt: new Date(),
    updatedAt: new Date()
  } as Teacher

  beforeEach(() => {
    pathService = 'api/teacher';

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });

    teacherService = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  it('should get all teachers', () => {

    // Given
    const mockTeachers: Teacher[] = [mockTeacher];

    // When
    teacherService.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
    });
    const req = httpTestingController.expectOne(`${pathService}`);

    // Then
    expect(req.request.method).toEqual('GET');
    req.flush(mockTeachers);
  });

  it('should get teacher detail', () => {

    // Given
    const teacherId = '1';

    // When
    teacherService.detail(teacherId).subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
    });
    const req = httpTestingController.expectOne(`${pathService}/${teacherId}`);

    // Then
    expect(req.request.method).toEqual('GET');
    req.flush(mockTeacher);
  });

});
