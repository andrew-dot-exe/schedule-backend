package ru.andrewexe.schedule.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.andrewexe.schedule.dto.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Обработка конкретного бизнес-исключения
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
//        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }

    // Обработка ошибок валидации
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ErrorResponseDto("BAD_REQUEST", "Ошибка валидации"), HttpStatus.BAD_REQUEST);
    }

    // Общий перехватчик для всех остальных системных ошибок
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAll(Exception ex) {
        return new ResponseEntity<>(new ErrorResponseDto("INTERNAL_SERVER_ERROR", "Что-то пошло не так"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
