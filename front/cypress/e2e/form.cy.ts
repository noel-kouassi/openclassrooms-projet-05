describe('Form spec', () => {

  beforeEach(() => {

    cy.visit('/login')

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
        name: 'Session',
        description: 'Session test',
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
  });

  it('should create a session successful', () => {

    cy.get('button.mat-raised-button').contains('Create').click();
    cy.get('.create mat-card-title').should('contain', 'Create session')

    cy.intercept('POST', '/api/session', {
      body: {
        name: 'Session',
        description: 'Session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('createSession');

    cy.get('.create input[formControlName="name"]').type('Session');
    cy.get('.create input[formControlName="date"]').type('2024-03-15');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    const numberOfTimes = 1;
    for (let i = 0; i < numberOfTimes; i++) {
      cy.get('mat-select[formControlName="teacher_id"]').type('{downarrow}');
    }
    cy.get('mat-select[formControlName="teacher_id"]').type('{enter}');

    cy.get('textarea[formcontrolname="description"]').type('description of the session', {timeout: 10000, force: true});
    cy.get('.create button[type="submit"]').click();

    cy.contains('Session created !').should('exist')
    cy.url().should('include', '/sessions')
  })

  it('should update a session successful', () => {

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Session',
        description: 'Session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }).as('session')

    cy.get('button.mat-raised-button').contains('Edit').click();
    cy.get('.create mat-card-title').should('contain', 'Update session')

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Updated session',
        description: 'Session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('updateSession');

    cy.get('.create input[formControlName="name"]').type('Updated Session');
    cy.get('.create input[formControlName="date"]').type('2024-03-17');

    cy.get('mat-select[formControlName="teacher_id"]').click();
    const numberOfSelection = 1;
    for (let i = 0; i < numberOfSelection; i++) {
      cy.get('mat-select[formControlName="teacher_id"]').type('{downarrow}');
    }
    cy.get('mat-select[formControlName="teacher_id"]').type('{enter}');

    cy.get('textarea[formcontrolname="description"]').type('description of the session', {timeout: 10000, force: true});
    cy.get('.create button[type="submit"]').click();

    cy.contains('Session updated !').should('exist')
    cy.url().should('include', '/sessions')
  })
});
