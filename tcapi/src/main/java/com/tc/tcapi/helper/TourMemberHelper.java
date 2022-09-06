package com.tc.tcapi.helper;

import com.tc.core.response.BaseResponse;
import com.tc.core.response.TourUserBadRequestResponse;
import com.tc.core.response.TourUserResponse;
import com.tc.tcapi.model.Tour;
import com.tc.tcapi.model.TourUser;
import com.tc.tcapi.model.User;
import com.tc.tcapi.service.TourService;
import com.tc.tcapi.service.TourUserService;
import com.tc.tcapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TourMemberHelper {

    private final TourUserService tourUserService;
    private final TourService tourService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> requestJoinTour(Long tourId) {
        Tour tour = tourService.getAvailableTour(tourId);
        if (tour == null) {
            return BaseResponse.badRequest("Tour was not available");
        }
        TourUser tourUser = new TourUser();
        tourUser.setTour(tour);
        tourUser.setUser(userService.getCurrentUser());
        // set requested status
        tourUser.setStatus(1);
        tourUserService.save(tourUser);
        //TODO: notify to tour owner

        return BaseResponse.badRequest("Request join tour success");
    }

    public ResponseEntity<?> acceptUser(Long tourUserId) {
        User currentUser = userService.getCurrentUser();
        Tour tour = tourService.getByOwnerAndTourUser(currentUser, tourUserId);
        if (tour == null) {
            return BaseResponse.badRequest("Can not find your tour have tour user id: " + tourUserId);
        }
        TourUser tourUser = new TourUser();
        tourUser.setTour(tour);
        tourUser.setUser(currentUser);
        // set accepted status
        tourUser.setStatus(2);
        tourUserService.save(tourUser);
        //TODO: notify to accepted owner

        return BaseResponse.badRequest("Accept tour user: " + tourUserId + " success!");
    }


    public ResponseEntity<?> getTourUsers(Long tourId) {
        List<TourUser> tourUsers = tourUserService.getTourUsers(tourId, userService.getCurrentUser());
        List<TourUserResponse> data = tourUsers.stream()
                .map(tourUser -> modelMapper.map(tourUser, TourUserResponse.class)).collect(Collectors.toList());
        return BaseResponse.success(data, "Get tour users success!");
    }

    public ResponseEntity<?> approveUser(Long tourUserId, int approveStatus) {
        User currentUser = userService.getCurrentUser();
        switch (approveStatus) {
            // reject
            case 0: {
                TourUser tourUser = tourUserService.getMemberOfMyTour(currentUser, tourUserId);
                if (tourUser != null) {
                    tourUser.setStatus(0);
                    tourUserService.save(tourUser);
                    log.info("Update tour user id {} status {}", tourUserId, 0);
                }
                break;
            }
            // accept
            case 2: {
                TourUser tourUser = tourUserService.getMemberOfMyTour(currentUser, tourUserId);
                User user = tourUser.getUser();
                boolean availableTourUser = tourUserService.availableTourUser(user.getId());
                if (availableTourUser) {
                    tourUser.setStatus(2);
                    tourUserService.save(tourUser);
                    log.info("Update tour user id {} status {}", tourUserId, 2);
                    return ResponseEntity.ok().build();
                } else {
                    tourUser.setStatus(4);
                    tourUserService.save(tourUser);
                    log.info("User could not available at this moment tourUserId: {} ", tourUserId);
                    return BaseResponse.badRequest(TourUserBadRequestResponse.userNotAvailable());
                }
            }
            default:
                return BaseResponse.badRequest("Approve status not valid!");
        }
        return ResponseEntity.ok().build();
    }
}
