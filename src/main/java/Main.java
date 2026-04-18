import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Path allClubs;
    private Path currentClub;
    private Path drawnClubs;

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

    private void initialize() throws IOException, URISyntaxException {

        final var currentDir = Paths
                .get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .getParent();
        allClubs = currentDir.resolve("allClubs.txt");
        currentClub = currentDir.resolve("currentClub.txt");
        drawnClubs = currentDir.resolve("drawnClubs.tsv");

        if (!Files.exists(currentClub)) {
            Files.createFile(currentClub);
        }
        var shouldInitilizeFiles = List.of(allClubs, drawnClubs);
        for (var f : shouldInitilizeFiles) {
            if (Files.exists(f)) {
                continue;
            }
            try (
                    var is = getClass().getResourceAsStream("/" + f.getFileName());
                    var os = Files.newOutputStream(f, StandardOpenOption.CREATE)
            ) {
                //noinspection DataFlowIssue
                is.transferTo(os);
            }
        }
    }

    private String readCurrent() throws IOException {
        return Files.readString(currentClub, StandardCharsets.UTF_8);
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
                continue;
            }
            return student;
        }
    }
    private void removeCurrent() throws IOException {
        Files.writeString(currentClub, "", StandardCharsets.UTF_8 ,StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeDrew(final String club, final String student) throws IOException {
        try {
            Files.writeString(drawnClubs, club + "\t" + student + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("描き終えた部活一覧への書き込みに失敗しました。ロールバックします。部活: 「" + club + "」、生徒: 「" + student + "」");
            writeCurrent(club);
            throw e;
        }
    }

    private void writeCurrent(final String club) throws IOException {
        Files.writeString(currentClub, club, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

    }
    private String choose() throws IOException {
        final var clubs = Files.readAllLines(allClubs, StandardCharsets.UTF_8);
        if (clubs.isEmpty()) {
            return "";
        }
        final var chosen = clubs.remove(new SecureRandom().nextInt(clubs.size()));
        writeCurrent(chosen);
        Files.write(allClubs, clubs, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        return chosen;
    }
}
