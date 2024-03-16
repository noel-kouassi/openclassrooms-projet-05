describe('List spec', () => {

  it('should list session information successful', () => {

    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [{name: 'First session',
        description: 'Session test',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()}]).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('.list mat-card .mat-card-title').should('contain', 'Rentals available')
    cy.get('.list mat-card .items .item .mat-card-title').should('contain', 'First session')
    cy.get('.list mat-card .items .item .mat-card-subtitle').should('contain', 'Session on')
    cy.get('.list mat-card .items .item .mat-card-content').should('contain', 'Session test')
  })

});
