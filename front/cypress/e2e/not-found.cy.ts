describe('Not found spec', () => {

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
  });

  it('should display a not found page when url not exists', () => {
    cy.visit('/loginTest')
    cy.url().should('include', '/404')
    cy.contains('Page not found !').should('be.visible');
  })
});
