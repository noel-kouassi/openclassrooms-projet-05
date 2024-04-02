describe('Detail spec', () => {

  beforeEach(() => {
    cy.visit('/login')
  });

  it('should display a session detail information successful', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName 1',
        firstName: 'firstName 1',
        lastName: 'lastName',
        admin: true
      }
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [{
        id: 1,
        lastName: 'lastname',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [{
        id: 1,
        lastName: 'test',
        firstName: 'teacher name',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.get('button.mat-raised-button').contains('Detail').click();
    cy.get('.m3 .mat-card .mat-card-content .ml1').should('contain', '2 attendees')
    cy.get('.m3 .mat-card .mat-card-content .description').should('contain', 'Description: session test')
  })


  it('should delete a session successfully', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName 1',
        firstName: 'firstName 1',
        lastName: 'lastName',
        admin: true
      }
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [{
        id: 1,
        lastName: 'lastname',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [{
        id: 1,
        lastName: 'test',
        firstName: 'teacher name',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/1',
      },
      {}).as('sessionDelete')

    cy.get('button.mat-raised-button').contains('Detail').click();
    cy.get('button[color="warn"]').click();
    cy.get('.mat-snack-bar-container').should('contain.text', 'Session deleted');
  })

  it('should go back on previous page', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName 1',
        firstName: 'firstName 1',
        lastName: 'lastName',
        admin: true
      }
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [{
        id: 1,
        lastName: 'lastname',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [{
        id: 1,
        lastName: 'test',
        firstName: 'teacher name',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.get('button.mat-raised-button').contains('Detail').click();
    cy.get('button[mat-icon-button]').click();
    cy.url().should('include', '/sessions')
  })


  it('should unparticipate to a session', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName 1',
        firstName: 'firstName 1',
        lastName: 'lastName',
        admin: false
      }
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1,2],
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [{
        id: 1,
        lastName: 'lastname',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [1,2],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [{
        id: 1,
        lastName: 'test',
        firstName: 'teacher name',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/1/participate/1',
      },
      {});

    cy.get('button.mat-raised-button').contains('Detail').click();
    cy.get('button[color="warn"]').click();
  })


  it('should participate to a session', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName 1',
        firstName: 'firstName 1',
        lastName: 'lastName',
        admin: false
      }
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [{
        id: 1,
        lastName: 'lastname',
        firstName: 'firstName',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'session name',
        description: 'session test',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [{
        id: 1,
        lastName: 'test',
        firstName: 'teacher name',
        createdAt: new Date(),
        updatedAt: new Date()
      }]).as('teacher')

    cy.intercept(
      {
        method: 'POST',
        url: '/api/session/1/participate/1',
      },
      {});

    cy.get('button.mat-raised-button').contains('Detail').click();
    cy.get('button[color="primary"]').click();
  })
});
