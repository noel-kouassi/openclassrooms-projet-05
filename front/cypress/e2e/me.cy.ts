describe('Me spec', () => {

  it('should display user information successful', () => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'alex',
        firstName: 'first user',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'test',
        firstName: 'first user',
        admin: true,
        password: 'test!1234',
        createdAt: new Date
      }
    })

    cy.get('.link').contains('Account').click()

    cy.contains('p', 'Name: first user TEST')
    cy.contains('p', 'Email: yoga@studio.com')
    cy.contains('.my2', 'You are admin')
  })

  it('should back to previous page', () => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'alex',
        firstName: 'first user',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'test',
        firstName: 'first user',
        admin: true,
        password: 'test!1234',
        createdAt: new Date
      }
    })

    cy.get('.link').contains('Account').click()

    cy.get('button[mat-icon-button]').click();
    cy.url().should('include', '/sessions')

  })

  it('should make delete action', () => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'alex',
        firstName: 'first user',
        lastName: 'lastName',
        admin: false
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'test',
        firstName: 'first user',
        admin: false,
        password: 'test!1234',
        createdAt: new Date
      }
    })

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/user/1',
      },
      {}).as('userDelete')

    cy.get('.link').contains('Account').click()
    cy.get('mat-card .mat-card-content').should('contain', 'Delete my account:')
    cy.get('button[color="warn"]').click();

    cy.get('.mat-snack-bar-container').should('contain.text', 'Your account has been deleted');
  })
});
