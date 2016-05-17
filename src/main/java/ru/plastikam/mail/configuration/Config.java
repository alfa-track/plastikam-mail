package ru.plastikam.mail.configuration;

public class Config {

    public static class Mail {

        public static final long scheduleDelayMs = 5000;

        public static final long maxMailPerRound = 5;

        public static class In {

            //            public static final String username = "conf5843@gmail.com";
//            public static final String password = "321qazxc";
            public static final String username = "Recover@plastikam-team.ru";

            public static final String password = "slonslon";
        }

        public static class Out {

            public static final String username = "conf5843@gmail.com";

            public static final String password = "321qazxc";

//            public static final String username = "Recover@plastikam-team.ru";
//            public static final String password = "slonslon";

            public static final String recipient = "roistat.plastikam@gmail.com";
        }

    }

}
