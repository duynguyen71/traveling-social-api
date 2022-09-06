package com.tc.tcapi.helper;

import com.tc.core.request.LocationRequest;
import com.tc.core.request.TagRequest;
import com.tc.core.request.TourRequest;
import com.tc.core.response.*;
import com.tc.tcapi.model.*;
import com.tc.tcapi.request.BaseParamRequest;
import com.tc.tcapi.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TourHelper {

    private final TourService tourService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TourUserService tourUserService;
    private final TagService tagService;
    private final LocationService locationService;
    private final ChatGroupService chatGroupService;

    public ResponseEntity<?> getCreatedTours(Map<String, String> param) {
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        Pageable pageable = baseParamRequest.toNativePageRequest("createDate");
        User currentUser = userService.getCurrentUser();
        List<Tour> createdTours = tourService.getCreatedTours(currentUser, pageable);
        List<BaseTourResponse> collect = createdTours.stream().map(t -> {
            BaseTourResponse map = modelMapper.map(t, BaseTourResponse.class);
            map.setJoinedMember(tourUserService.countJoinedUser(t));
            return map;
        }).collect(Collectors.toList());
        return BaseResponse.success(collect, "Get created tours success!");
    }

    public ResponseEntity<?> getAvailableTours() {
        return null;
    }


    /***
     * Create a tour
     * @param request
     * @return
     */
    public ResponseEntity<?> createTour(TourRequest request) {
        String title = request.getTitle();
        String content = request.getContent();
        Date departureDate = request.getDepartureDate();
        int numOfMember = request.getNumOfMember();
        double cost = request.getCost();
        Long id = request.getId();
        Tour tour;
        User currentUser = userService.getCurrentUser();
        if (id == null || !tourService.existByUserAndId(id, currentUser))
            tour = new Tour();
        else tour = tourService.getByIdAndUser(id, currentUser);
        tour.setUser(currentUser);
        tour.setTitle(title);
        tour.setContent(content);
        tour.setClose(request.isClose());
        tour.setCost(cost);
        tour.setDepartureDate(departureDate);
        tour.setNumOfMember(numOfMember);
        tour = tourService.saveFlush(tour);
        // Save location
        LocationRequest locationRequest = request.getLocation();
        if (locationRequest.getId() == null) {
            Location location = new Location();
            location.setType(2);
            location.setStatus(1);
            location.setLabel(locationRequest.getLabel());
            location.setName(locationRequest.getName());
            location.setLongitude(locationRequest.getLongitude());
            location.setLatitude(locationRequest.getLatitude());
            location.setCity(locationRequest.getCity());
            location.setCountryCode(locationRequest.getCountryCode());
            location.setCountryName(locationRequest.getCountryName());
            location = locationService.saveFlush(location);
            tour.setLocation(location);
            tourService.save(tour);
        }
        //
        Set<TagRequest> requestTags = request.getTags();
        for (TagRequest tagRequest :
                requestTags) {
            Tag tag;
            String name = tagRequest.getName();
            tag = tagService.getByName(name);
            if (tag == null) {
                tag = tagService.saveFlush(Tag.builder().name(name).build());
            }
            if (tagRequest.getStatus() == 0) {
                tour.getTags().remove(tag);
            } else {
                tour.getTags().add(tag);
            }
            tourService.save(tour);
        }

        return BaseResponse.success(modelMapper.map(tour, BaseTourResponse.class), "Create tour success!");

    }

    /**
     * Close or Open a tour
     *
     * @param tourId
     * @param isClose
     * @return
     */
    public ResponseEntity<?> closeTour(long tourId, boolean isClose) {
        User currentUser = userService.getCurrentUser();
        Tour tour = tourService.getByIdAndUser(tourId, currentUser);
        if (tour != null) {
            tour.setClose(isClose);
            tourService.save(tour);
            return BaseResponse.success("Update tour is close: " + isClose);
        }
        return BaseResponse.success("Can not find your tour with id: " + tourId);
    }

    public ResponseEntity<?> getTourDetail(Long id) {
        User currentUser = userService.getCurrentUser();
        Tour tour = tourService.getByIdAndUser(id, currentUser);
        if (tour == null) {
            return BaseResponse.badRequest("Can not find tour with id: " + id);
        }
        TourDetailResponse detail = modelMapper.map(tour, TourDetailResponse.class);
        return BaseResponse.success(detail, "Get tour detail success");
    }

    public ResponseEntity<?> getCurrentTour() {
        User currentUser = userService.getCurrentUser();
        Long currentUserId = currentUser.getId();
        Tour currentJoinTour = tourService.getCurrentJoinTour(currentUser);
        if (currentJoinTour != null) {
            CurrentJoinTourResponse map = modelMapper.map(currentJoinTour, CurrentJoinTourResponse.class);
            List<TourUser> approvedUsersInTour = tourUserService.getApprovedUsersInTour(currentJoinTour.getId(), currentUserId);
            map.setUsers(approvedUsersInTour.stream()
                    .map(tourUser -> {
                        TourUserResponse tourUserResponse = modelMapper.map(tourUser, TourUserResponse.class);
                        chatGroupService.getGroupByTwoUsers(currentUserId, tourUser.getUser().getId());
                        return tourUserResponse;
                    }).collect(Collectors.toList()));
            map.setNumOfRequest(tourUserService.countRequestUser(currentJoinTour));
            map.setHost(modelMapper.map(currentJoinTour.getUser(), BaseUserResponse.class));
            return BaseResponse.success(map, "Get current tour success");
        }
        return BaseResponse.badRequest("Can not find current tour");
    }
}
