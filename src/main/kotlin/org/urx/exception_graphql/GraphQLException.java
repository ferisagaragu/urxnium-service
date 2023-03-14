package org.urx.exception_graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class GraphQLException extends RuntimeException implements GraphQLError {

  private ErrorType errorType;

  public GraphQLException(String message) {
    super(message);
  }

  public GraphQLException(String message, ErrorType errorType) {
    super(message);
    this.errorType = errorType;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return null;
  }

  @Override
  public ErrorType getErrorType() {
    if (errorType != null) return errorType;
    return null;
  }

  @Override
  public StackTraceElement[] getStackTrace() {
    return new StackTraceElement[] { };
  }

}