import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {SessionApiService} from './session-api.service';
import {Session} from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let sessionApiService: SessionApiService;
  let httpTestingController: HttpTestingController;
  let pathService: string;

  const mockSession = {
    name: 'test',
    description: 'test',
    date: new Date(),
    teacher_id: 1,
    users: [1]
  } as Session;

  beforeEach(() => {
    pathService = 'api/session';

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    sessionApiService = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });

  it('should get all sessions', () => {

    // Given
    const mockSessions: Session[] = [mockSession];

    // When
    sessionApiService.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });
    const req = httpTestingController.expectOne(`${pathService}`);

    // Then
    expect(req.request.method).toEqual('GET');
    req.flush(mockSessions);
  });

  it('should get session detail', () => {

    // Given
    const sessionId = '1';

    // When
    sessionApiService.detail(sessionId).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpTestingController.expectOne(`${pathService}/${sessionId}`);

    // Then
    expect(req.request.method).toEqual('GET');
    req.flush(mockSession);
  });

  it('should delete a session', () => {

    // Given
    const sessionId = '1';

    // When
    sessionApiService.delete(sessionId).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/${sessionId}`);

    // Then
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });

  it('should create a session', () => {

    // When
    sessionApiService.create(mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpTestingController.expectOne(`${pathService}`);

    // Then
    expect(req.request.method).toEqual('POST');
    req.flush(mockSession);
  });

  it('should update a session', () => {

    // Given
    const sessionId = '1';

    // When
    sessionApiService.update(sessionId, mockSession).subscribe((session) => {
      expect(session).toEqual(mockSession);
    });
    const req = httpTestingController.expectOne(`${pathService}/${sessionId}`);

    // Then
    expect(req.request.method).toEqual('PUT');
    req.flush(mockSession);
  });

  it('should participate in a session', () => {

    // Given
    const sessionId = '1';
    const userId = '1';

    // When
    sessionApiService.participate(sessionId, userId).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/${sessionId}/participate/${userId}`);

    // Then
    expect(req.request.method).toEqual('POST');
    req.flush(null);
  });

  it('should unparticipate in a session', () => {

    // Given
    const sessionId = '1';
    const userId = '1';

    // When
    sessionApiService.unParticipate(sessionId, userId).subscribe();
    const req = httpTestingController.expectOne(`${pathService}/${sessionId}/participate/${userId}`);

    // Then
    expect(req.request.method).toEqual('DELETE');
    req.flush(null);
  });
});
