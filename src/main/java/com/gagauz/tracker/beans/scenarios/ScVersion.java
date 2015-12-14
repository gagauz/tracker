package com.gagauz.tracker.beans.scenarios;

import com.gagauz.tracker.beans.dao.*;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.*;
import com.gagauz.tracker.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ScVersion")
public class ScVersion extends DataBaseScenario {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TicketStatusDao statusDao;

    @Autowired
    private TicketTypeDao typeDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private VersionDao versionDao;

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private FeatureVersionDao featureVersionDao;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private WorkLogDao workLogDao;

    @Autowired
    private TicketCommentDao ticketCommentDao;

    @Autowired
    private ScUser scUser;

    @Autowired
    private ScStatus scStatus;

    private String[] fnames = {"Страница регистрации и авторизация", "Каталог товаров", "Главная страница", "Страница успешного оформления заказа",
            "Страница оформления заказа", "Личный кабинет - Персональные данные", "Личный кабинет - Адреса", "Личный кабинет - Заказы"};

    @Override
    protected void execute() {
        User user1 = userDao.findById(1);
        User user2 = userDao.findById(2);

        for (int i = 0; i < 1; i++) {
            Project project = new Project();
            project.setKey1("TRACKER");
            project.setName("Трекер (этот проект)");
            CvsRepo repo = new CvsRepo();
            if (true) {
                repo.setType(CvsType.GIT);
                repo.setUrl("https://github.com/gagauz/tracker.git");
                repo.setUsername("gagauz");
                repo.setPassword("p35neog0d");
                repo.setBranch("master");
            } else {
                repo.setType(CvsType.SVN);
                repo.setUrl("file:///R:/projects-my/tracker-svn-repo");
                //                repo.setBranch("trunk");
            }
            project.setCvsRepo(repo);
            projectDao.save(project);

            List<Feature> features = new ArrayList<>();

            for (int h = 0; h < 10; h++) {
                Feature feature = new Feature();
                feature.setProject(project);
                feature.setCreator(user1);
                feature.setName(RandomUtils.getRandomObject(fnames));
                feature.setDescription("Общее описание фичи, например, Авторизация на сайте для доступа к защищенным разделам.");
                features.add(feature);
            }
            featureDao.save(features);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            for (int j = 0; j < 3; j++) {
                Version version = new Version();
                version.setReleased(j == 0);
                version.setProject(project);
                if (j != 4) {
                    version.setName("TRACKER-1." + j);
                    version.setReleaseDate(cal.getTime());
                    cal.add(Calendar.MONTH, 1);
                } else {
                    version.setName("1." + j + "-SNAPSHOT");
                    cal.setTime(new Date());
                    cal.add(Calendar.MONTH, 1);
                    version.setReleaseDate(cal.getTime());
                }

                versionDao.save(version);
                for (Feature feature : features) {
                    if (rand.nextBoolean() && rand.nextBoolean()) {
                        continue;
                    }

                    FeatureVersion featureVersion = new FeatureVersion();
                    featureVersion.getId().setFeature(feature);
                    featureVersion.getId().setVersion(version);
                    featureVersion.setCreator(user2);
                    featureVersionDao.save(featureVersion);

                    int stc = rand.nextInt(5) + 1;
                    for (int k = 0; k < stc; k++) {
                        Ticket ticket = new Ticket();
                        ticket.setType(getRandomType());
                        ticket.setFeatureVersion(featureVersion);
                        ticket.setOwner(getRandomUser());
                        ticket.setAuthor(getRandomUser());
                        ticket.setSummary("Регистрация");
                        ticket.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
                        int es = 15 * (rand.nextInt(10) + 1);
                        ticket.setEstimate(es);
                        WorkLog wl = null;
                        ticket.setStatus(getRandomStatus());
                        if (version.isReleased() || rand.nextBoolean()) {
                            wl = new WorkLog();
                            wl.setTicket(ticket);
                            wl.setUser(ticket.getAuthor());
                            if (!version.isReleased()) {
                                wl.setLogTime(es / (rand.nextInt(5) + 1));
                            } else {
                                wl.setLogTime(es);
                            }
                        }
                        if (rand.nextBoolean()) {
                            Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                            Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                            ticket.setAttachments(Arrays.asList(a1, a2));
                        }
                        List<TicketComment> cms = new ArrayList<>();
                        if (rand.nextBoolean()) {
                            for (int x = rand.nextInt(10) + 1; x > 0; x--) {
                                TicketComment cm = new TicketComment();
                                cm.setAuthor(getRandomUser());
                                cm.setTicket(ticket);
                                cm.setText("Lorem ipsum dolorsit.");

                                if (rand.nextBoolean()) {
                                    Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                                    Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                                    cm.setAttachments(Arrays.asList(a1, a2));
                                }
                                cms.add(cm);
                            }
                        }
                        ticketDao.save(ticket);
                        if (wl != null) {
                            workLogDao.save(wl);
                        }
                        ticketDao.updateTicketProgessTime(ticket);
                        if (!cms.isEmpty()) {
                            ticketCommentDao.save(cms);
                        }

                        int stc1 = rand.nextInt(3) + 1;
                        for (int k1 = 0; k1 < stc1; k1++) {
                            Ticket bug = new Ticket();
                            bug.setFeatureVersion(featureVersion);
                            bug.setParent(ticket);
                            bug.setType(getRandomType());
                            bug.setStatus(getRandomStatus());
                            bug.setOwner(ticket.getOwner());
                            bug.setAuthor(getRandomUser());
                            bug.setSummary("Bug/ Регистрация");
                            bug.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
                            ticketDao.save(bug);
                        }
                    }
                }
            }
        }
    }

    private Map<Integer, TicketStatus> statusHash;
    private Map<Integer, TicketType> typeHash;
    private Map<Integer, User> userHash;

    private TicketStatus getRandomStatus() {
        if (null == statusHash) {
            statusHash = new HashMap<>();
            for (TicketStatus s : statusDao.findAll()) {
                statusHash.put(s.getId(), s);
            }
        }
        return RandomUtils.getRandomObject(statusHash.values());
    }

    private TicketType getRandomType() {
        if (null == typeHash) {
            typeHash = new HashMap<>();
            for (TicketType s : typeDao.findAll()) {
                typeHash.put(s.getId(), s);
            }
        }
        return RandomUtils.getRandomObject(typeHash.values());
    }

    private User getRandomUser() {
        if (null == userHash) {
            userHash = new HashMap<>();
            for (User u : userDao.findAll()) {
                userHash.put(u.getId(), u);
            }
        }
        return RandomUtils.getRandomObject(userHash.values());
    }

    @Override
    protected DataBaseScenario[] getDependsOn() {
        return new DataBaseScenario[] {scUser, scStatus};
    }
}
