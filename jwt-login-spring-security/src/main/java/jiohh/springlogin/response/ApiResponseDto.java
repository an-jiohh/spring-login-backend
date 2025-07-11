package jiohh.springlogin.response;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {
    private final String status = "success";
    private final T data;

    public ApiResponseDto(T data) {
        this.data = data;
    }

    public static <T> ApiResponseDto<T> success(T data){
        return new ApiResponseDto<>(data);
    }

    public static ApiResponseDto<Void> successWithNoData() {
        return new ApiResponseDto<>(null);
    }
}
