package org.apache.olingo.jpa.processor.core.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Subquery;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Unary;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

public class JPALambdaAllOperation extends JPALambdaOperation {

  JPALambdaAllOperation(JPAFilterComplierAccess jpaComplier, UriInfoResource member) {
    super(jpaComplier, member);
  }

  public Subquery<?> getNotExistsQuery() throws ODataApplicationException {
    // TODO Auto-generated method stub
    return getSubQuery(new NotExpression(determineExpression()));
  }

  @Override
  public Expression<Boolean> get() throws ODataApplicationException {
    CriteriaBuilder cb = converter.cb;
    return cb.and(cb.exists(getExistsQuery()), cb.not(cb.exists(getNotExistsQuery())));
  }

  private class NotExpression implements Unary {
    private final org.apache.olingo.server.api.uri.queryoption.expression.Expression expression;

    public NotExpression(org.apache.olingo.server.api.uri.queryoption.expression.Expression expression) {
      super();
      this.expression = expression;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
      T operand = expression.accept(visitor);
      return visitor.visitUnaryOperator(getOperator(), operand);
    }

    @Override
    public org.apache.olingo.server.api.uri.queryoption.expression.Expression getOperand() {
      return expression;
    }

    @Override
    public UnaryOperatorKind getOperator() {
      return UnaryOperatorKind.NOT;
    }

  }

}