describe('AuthGuard', () => {
  it('should redirect to login page when user is not logged in', () => {
    cy.clearCookies();
    cy.clearLocalStorage();
    cy.visit('/sessions');
    cy.url().should('include', '/login');
  });
});
