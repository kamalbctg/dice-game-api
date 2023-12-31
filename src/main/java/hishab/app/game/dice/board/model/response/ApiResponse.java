package hishab.app.game.dice.board.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private T data;

    public static <T> ApiResponse<T> successWithNoMessage(T data) {
        return new ApiResponse<>(200, data);
    }

    public static <T> ApiResponse<T> error(int status, T error) {
        return new ApiResponse<>(status, error);
    }
}