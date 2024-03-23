describe('Register spec', () => {

  beforeEach(() => {
    cy.visit('/register');
  });

  it('should fill out the registration form and submit successfully', () => {

    cy.intercept('POST', '/api/auth/register', {
      body: {
        firstName: 'Alex',
        lastName: 'Test',
        email: 'alex.test@test.com',
        password: 'test1234'
      },
    })

    cy.get('[formControlName="firstName"]').type('Alex');
    cy.get('[formControlName="lastName"]').type('Test');
    cy.get('[formControlName="email"]').type('alex.test@test.com');
    cy.get('[formControlName="password"]').type('test1234');

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/login');
  });

  it('should display an error message on registration failure', () => {

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: {error: 'Internal Server Error'},
    }).as('registerFailure');

    cy.get('[formControlName="firstName"]').type('Alex');
    cy.get('[formControlName="lastName"]').type('Test');
    cy.get('[formControlName="email"]').type('alex.test@test.com');
    cy.get('[formControlName="password"]').type('test1234');

    cy.get('button[type="submit"]').click();

    cy.get('.error').should('be.visible');
  });
});
