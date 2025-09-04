package elearningspringboot.exception;

import com.azure.core.exception.ResourceNotFoundException;
import elearningspringboot.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationBody(MethodArgumentNotValidException e, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid data in request body", request, e.getBindingResult().getFieldErrors().stream().map(ex -> ErrorResponse.FieldError.builder()
                .fieldName(ex.getField())
                .message(ex.getDefaultMessage())
                .build()).collect(Collectors.toList()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationParams(ConstraintViolationException e, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid data in request body", request, e.getConstraintViolations().stream().map(v -> ErrorResponse.FieldError.builder()
                .fieldName(v.getPropertyPath().toString().substring(v.getPropertyPath().toString().lastIndexOf(".") + 1))
                .message(v.getMessage())
                .build()).collect(Collectors.toList()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTypeParams(MethodArgumentTypeMismatchException e, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getName() + " must be of type " + Objects.requireNonNull(e.getRequiredType()).getSimpleName(), request, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException e, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Missing params " + e.getParameterName(), request, null);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Invalid data in request body";

        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {
            String fieldName = "Unknown field";

            if (!ife.getPath().isEmpty()) {
                fieldName = ife.getPath().get(0).getFieldName();
            }

            Class<?> targetType = ife.getTargetType();

            if (targetType == Integer.class || targetType == Long.class || targetType == Double.class) {
                message = String.format("Field '%s' must be a number", fieldName);
            } else if (targetType == java.time.LocalDate.class || targetType == java.util.Date.class) {
                message = String.format("Field '%s' must be a valid date with format dd/MM/yyyy", fieldName);
            }
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request, null);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e, WebRequest request){
        System.out.println("RESOURCE NOT FOUND");
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request, null);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleResourceConflict(ResourceConflictException e, WebRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request, null);
    }

    @ExceptionHandler({BadCredentialsException.class, DisabledException.class, UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(RuntimeException e, WebRequest request){
        HttpStatus status;
        String message;
        if (e instanceof BadCredentialsException){
            status = HttpStatus.UNAUTHORIZED;
            message = "Invalid email or password";
        } else if (e instanceof DisabledException) {
            status = HttpStatus.FORBIDDEN;
            message = "Account is banned or pending";
        } else {
            status = HttpStatus.UNAUTHORIZED;
            message = e.getMessage();
        }
        return buildErrorResponse(status, message, request, null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException e, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Access Denied", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception e, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request, null);
    }


    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppError(AppException e, WebRequest request) {
        return ResponseEntity.status(e.getErrorCode().getCode()).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(e.getErrorCode().getCode())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .error(e.getMessage())
                        .message(e.getMessage())
                        .errors(null)
                        .build()
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, WebRequest request, List<ErrorResponse.FieldError> errors) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .error(status.getReasonPhrase())
                        .message(message)
                        .errors(errors)
                        .build()
        );
    }

}
