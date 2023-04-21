package com.company.enroller.controllers;


import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();

        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {

        if (meetingService.findById(meeting.getId()) != null) {

            return new ResponseEntity<String>("Unable to create. A meeting with id " + meeting.getId() + "already exist.", HttpStatus.CONFLICT);
        }

        meetingService.add(meeting);

        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        meetingService.delete(meeting);

        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Meeting updatedMeeting) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        meeting.setTitle(updatedMeeting.getTitle());
        meeting.setDescription(updatedMeeting.getDescription());
        meeting.setDate(updatedMeeting.getDate());

        meetingService.update(meeting);

        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.PUT)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable Long id, @PathVariable String login) {

        Meeting meeting = meetingService.findById(id);

        Participant participant = participantService.findByLogin(login);

        if (participant == null || meeting == null) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        meetingService.addParticipantToMeeting(meeting, participant);

        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromMeeting(@PathVariable Long id, @PathVariable String login) {

        Meeting meeting = meetingService.findById(id);

        Participant participant = participantService.findByLogin(login);

        if (participant == null || meeting == null) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        meetingService.removeParticipantFromMeeting(meeting, participant);

        return new ResponseEntity<Meeting>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable Long id) {

        Meeting meeting = meetingService.findById(id);

        if (meeting == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participants = meeting.getParticipants();

        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

}
