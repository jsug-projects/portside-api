package sccsample;

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

    public static void createData(PlatformTransactionManager tm,
                           SessionRepository sessionRepository,
                           SpeakerRepository speakerRepository,
                           AttendeeRepository attendeeRepository
                           ) {
        TransactionStatus st = tm.getTransaction(null);

        List<Speaker> speakers = new ArrayList<>();
        for (int i=0; i<7; i++) {
            Speaker speaker = createSpeaker(i);
            speakerRepository.save(speaker);
            speakers.add(speaker);
//            if (i==0) {
//                speakerIdFixture1 = speaker.id;
//            }
//            if (i==1) {
//                speakerIdFixture2 = speaker.id;
//            }

        }


        List<Session> sessions = new ArrayList<>();
        for (int i=0; i<10; i++) {
            Session session = createSession(i);
            sessionRepository.save(session);
//            if (i==0) {
//                sessionIdFixture = session.id;
//            }

            session.assignSpeakers(speakers);

            sessions.add(session);
        }



        for (int i=0; i<5; i++) {
            Attendee attendee = createAttendee(i);
            attendeeRepository.save(attendee);
            for (Session session : sessions) {
                session.attended(attendee);
            }
//            if (i==0) {
//                attendeeIdFixture = attendee.id;
//            }
        }


        tm.commit(st);

    }
    static Session createSession(int i) {
        Session session = new Session();
        session.title = "ダミーセッション"+i;
        session.speaker = "ダミースピーカー"+i;
        session.description = "ダミー概要"+i;
        return session;
    }

    static Speaker createSpeaker(int i) {
        Speaker speaker = new Speaker();
        speaker.name = "名前"+i;
        speaker.belongTo ="所属"+i;
        speaker.profile = "プロフィール"+i;
        return speaker;
    }
    static Attendee createAttendee(int i) {
        Attendee attendee = new Attendee();
        attendee.email = "foo"+i+"@example.com";
        return attendee;
    }

}
