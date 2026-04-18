import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path ALL_CLUBS = Path.of("./allClubs.txt");
    private static final Path CURRENT_CLUB = Path.of("./currentClub.txt");
    private static final Path DRAWN_CLUBS = Path.of("./drawnClubs.tsv");

    private static final String OUTPUT_FORMAT = "「%s」を描いて下さい\n";
    private static final String CONFIRM_MESSAGE_FORMAT = "「%s」は描き終わりましたか？ y/n\n";

    void main() throws Exception {
        initialize();
        final var currentClub = readCurrent();
        if (currentClub.isEmpty()) {
            showNextClub();
            return;
        }
        if (!hasDrawnCurrentClub(currentClub)) {
            return;
        }
        final var drewStudent = askWhoDrew();
        removeCurrent();
        writeDrew(currentClub, drewStudent);
        showNextClub();
    }

    private void showNextClub() throws IOException {
        final var nextClub = choose();
        if (nextClub.isEmpty()) {
            System.out.println("全部描き終えています");
            return;
        }
        System.out.printf(OUTPUT_FORMAT, nextClub);
    }

    private void initialize() throws IOException {
        var files = List.of(CURRENT_CLUB, DRAWN_CLUBS);
        if (!Files.exists(CURRENT_CLUB)) {
            Files.createFile(CURRENT_CLUB);
        }
        var shouldInitilizeFiles = List.of(ALL_CLUBS, DRAWN_CLUBS);
        for (var f : shouldInitilizeFiles) {
            if (Files.exists(f)) {
                continue;
            }
            try (
                    var is = getClass().getResourceAsStream("/" + f.getFileName());
                    var os = Files.newOutputStream(f, StandardOpenOption.CREATE);
            ) {
                is.transferTo(os);
            }
        }
    }

    private String readCurrent() throws IOException {
        return Files.readString(CURRENT_CLUB, StandardCharsets.UTF_8);
    }

    private boolean hasDrawnCurrentClub(final String currentClub) {
        if (currentClub.isEmpty()) {
            return true;
        }

        System.out.printf(CONFIRM_MESSAGE_FORMAT, currentClub);
        while(true) {
            var s = new Scanner(System.in);
            var answer = s.nextLine();
            if ("y".equalsIgnoreCase(answer)) {
                return true;
            }
            if ("n".equalsIgnoreCase(answer)) {
                return false;
            }
            System.out.println("yまたはnを入力して下さい");
        }
    }

    private String askWhoDrew() {
        System.out.println("誰を描きましたか？");
        while(true) {
            var s = new Scanner(System.in);
            var student = s.nextLine();
            if (student.isEmpty()) {
                System.out.println("生徒名を入力して下さい");
            }
            return student;
        }
    }
    private void removeCurrent() throws IOException {
        Files.writeString(CURRENT_CLUB, "", StandardCharsets.UTF_8 ,StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeDrew(final String club, final String student) throws IOException {
        try {
            Files.writeString(DRAWN_CLUBS, club + "\t" + student + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("描き終えた部活一覧への書き込みに失敗しました。ロールバックします。部活: 「" + club + "」、生徒: 「" + student + "」");
            writeCurrent(club);
            throw e;
        }
    }

    private void writeCurrent(final String club) throws IOException {
        Files.writeString(CURRENT_CLUB, club, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

    }
    private String choose() throws IOException {
        final var clubs = Files.readAllLines(ALL_CLUBS, StandardCharsets.UTF_8);
        if (clubs.isEmpty()) {
            return "";
        }
        final var chosen = clubs.remove(new SecureRandom().nextInt(clubs.size()));
        writeCurrent(chosen);
        Files.write(ALL_CLUBS, clubs, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        return chosen;
    }
}
