package sccsample.provider;

import jsug.portside.api.entity.Attendee;
import jsug.portside.api.entity.Session;
import jsug.portside.api.entity.Speaker;
import jsug.portside.api.repository.AttendeeRepository;
import jsug.portside.api.repository.SessionRepository;
import jsug.portside.api.repository.SpeakerRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class SessionsFixture {

    public static void createData(
                           SessionRepository sessionRepository,
                           SpeakerRepository speakerRepository,
                           AttendeeRepository attendeeRepository
                           ) {

        List<Speaker> speakers = new ArrayList<>();
        for (int i=0; i<7; i++) {
            Speaker speaker = createSpeaker(i);
            speakerRepository.save(speaker);
            speakers.add(speaker);

        }


        List<Session> sessions = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Session session = createSession(i);
            sessionRepository.save(session);
            session.assignSpeakers(speakers);

            sessions.add(session);
        }



        for (int i=0; i<5; i++) {
            Attendee attendee = createAttendee(i);
            attendeeRepository.save(attendee);
            for (Session session : sessions) {
                session.attended(attendee);
            }
        }


    }
    public static Session createSession(int i) {
        Session session = new Session();
        session.title = "ダミーセッション"+i;
        session.speaker = "ダミースピーカー"+i;
        session.description = "ダミー概要"+i;
        return session;
    }

    public static Speaker createSpeaker(int i) {
        Speaker speaker = new Speaker();
        speaker.name = "名前"+i;
        speaker.belongTo ="所属"+i;
        speaker.profile = "プロフィール"+i;
        return speaker;
    }
    public static Attendee createAttendee(int i) {
        Attendee attendee = new Attendee();
        attendee.email = "foo"+i+"@example.com";
        return attendee;
    }

}
