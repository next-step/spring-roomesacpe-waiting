package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetail;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class WaitingController {

    @Autowired
    private WaitingService waitingService;

    @PostMapping("/reservation-waitings")
    public ResponseEntity createWaiting(@LoginMember UserDetail userDetail,
                                        @RequestBody WaitingRequest waitingRequest) {
        WaitingCreationResponse response = waitingService.create(userDetail, waitingRequest);
        if (response.isReserved()) {
            return ResponseEntity.created(URI.create("/reservations/" + response.getPrincipalId())).build();
        }
        return ResponseEntity.created(URI.create("/reservation-waitings/" + response.getPrincipalId())).build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity waitingMine(@LoginMember UserDetail userDetail) {
        List<WaitingResponse> responses = waitingService.findByUser(userDetail);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteWaiting(@LoginMember UserDetail userDetail, @PathVariable Long id) {
        waitingService.delete(userDetail, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
