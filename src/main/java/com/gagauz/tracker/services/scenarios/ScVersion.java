package com.gagauz.tracker.services.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.WorkLog;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.services.dao.FeatureDao;
import com.gagauz.tracker.services.dao.ProjectDao;
import com.gagauz.tracker.services.dao.TicketDao;
import com.gagauz.tracker.services.dao.TicketStatusDao;
import com.gagauz.tracker.services.dao.TicketTypeDao;
import com.gagauz.tracker.services.dao.UserDao;
import com.gagauz.tracker.services.dao.VersionDao;
import com.gagauz.tracker.services.dao.WorkLogDao;
import com.gagauz.tracker.services.dao.WorkflowDao;
import com.gagauz.tracker.utils.RandomUtils;
import com.xl0e.testdata.DataBaseScenario;

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
    private TicketDao ticketDao;

    @Autowired
    private WorkLogDao workLogDao;

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private ScUser scUser;

    @Autowired
    private ScTicketType scTicketType;

    @Autowired
    private ScStatus scStatus;

    @Autowired
    private ScProject scProject;

    private String[] fnames = { "Страница регистрации и авторизация", "Каталог товаров", "Главная страница",
            "Страница успешного оформления заказа",
            "Страница оформления заказа", "Личный кабинет - Персональные данные", "Личный кабинет - Адреса", "Личный кабинет - Заказы" };

    private Project project;

    @Override
    protected void execute() {
        User user1 = userDao.findByUsername("username0");
        User user2 = userDao.findByUsername("username1");

        for (int i = 0; i < 1; i++) {
            project = projectDao.findByCode(ScProject.TRACKER);
            assert (project != null);
            List<Feature> features = new ArrayList<>();

            for (int h = 0; h < 10; h++) {
                Feature feature = new Feature();
                feature.setProject(project);
                feature.setCreator(user1);
                feature.setName(RandomUtils.getRandomObject(fnames));
                feature.setDescription("Общее описание фичи, например, Авторизация на сайте для доступа к защищенным разделам.");
                features.add(feature);
            }
            featureDao.saveAll(features);

            for (int j = 0; j < 3; j++) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, j - 1);

                Version version = new Version();

                version.setProject(project);
                version.setName("TRACKER-1." + j);
                version.setReleaseDate(cal.getTime());
                version.setReleased(cal.getTime().before(new Date()));

                versionDao.save(version);
            }

            for (Feature feature : features) {
                for (int j = 0; j < 4; j++) {
                    if (rand.nextBoolean() && rand.nextBoolean()) {
                        continue;
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, j - 1);
                    String versionName = "TRACKER-1." + j;
                    Version version = versionDao.findByName(versionName);

                    int stc = rand.nextInt(5) + 1;
                    for (int k = 0; k < stc; k++) {
                        Ticket ticket = new Ticket();
                        ticket.setType(getRandomType());
                        ticket.setFeature(feature);
                        ticket.setVersion(version);
                        ticket.setOwner(getRandomUser());
                        ticket.setAuthor(getRandomUser());
                        ticket.setSummary("Регистрация");
                        ticket.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
                        int es = 15 * (rand.nextInt(10) + 1);
                        ticket.setEstimate(es);
                        WorkLog wl = null;
                        ticket.setStatus(getRandomStatus());
                        assert (ticket.getStatus() != null);
                        if (null != version && (version.isReleased() || rand.nextBoolean())) {
                            wl = new WorkLog();
                            wl.setTicket(ticket);
                            wl.setUser(ticket.getAuthor());
                            if (!version.isReleased()) {
                                wl.setLogTime(es / (rand.nextInt(5) + 1));
                            } else {
                                wl.setLogTime(es);
                                ticket.setStatus(getClosedStatus());
                            }
                        }
                        if (rand.nextBoolean()) {
                            Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
                            Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
                            ticket.setAttachments(Arrays.asList(a1, a2));
                        }
                        List<Workflow> cms = new ArrayList<>();
                        if (rand.nextBoolean()) {
                            for (int x = rand.nextInt(10) + 1; x > 0; x--) {
                                Workflow cm = new Workflow();
                                cm.setAuthor(getRandomUser());
                                cm.setTicket(ticket);
                                cm.setComment("Lorem ipsum dolorsit.");

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
                            workflowDao.saveAll(cms);
                        }

                        int stc1 = rand.nextInt(3) + 1;

                        if (null != version) {
                            for (int k1 = 0; k1 < stc1; k1++) {
                                Ticket bug = new Ticket();
                                bug.setFeature(feature);
                                bug.setVersion(version);
                                bug.setParent(ticket);
                                bug.setType(getBugType());
                                bug.setStatus(version.isReleased() ? getClosedStatus() : getRandomStatus());
                                bug.setOwner(ticket.getOwner());
                                bug.setAuthor(getRandomUser());
                                bug.setSummary("Ошибка 500");
                                bug.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
                                ticketDao.save(bug);
                            }
                        }
                    }
                }
            }
        }
    }

    private Map<Integer, TicketStatus> statusHash;
    private Map<Integer, TicketType> typeHash;
    private Map<Integer, User> userHash;

    private Map<Integer, TicketStatus> getsStatuses() {
        if (null == statusHash) {
            statusHash = new HashMap<>();
            for (TicketStatus s : statusDao.findByProject(project)) {
                statusHash.put(s.getId(), s);
            }
        }
        return statusHash;
    }

    private TicketStatus getRandomStatus() {
        return RandomUtils.getRandomObject(getsStatuses().values());
    }

    private TicketStatus getClosedStatus() {
        return getsStatuses().values().stream()
                .filter(s -> s.getName().equalsIgnoreCase("closed"))
                .findAny()
                .orElseGet(() -> getRandomStatus());
    }

    private Map<Integer, TicketType> getTicketTypes() {
        if (null == typeHash) {
            typeHash = new HashMap<>();
            for (TicketType s : typeDao.findByProject(project)) {
                typeHash.put(s.getId(), s);
            }
        }
        return typeHash;
    }

    private TicketType getRandomType() {
        return RandomUtils.getRandomObject(getTicketTypes().values());
    }

    private TicketType getBugType() {
        return getTicketTypes().values()
                .stream()
                .filter(t -> t.getName().equalsIgnoreCase("bug"))
                .findAny()
                .orElseGet(() -> getRandomType());
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
        return new DataBaseScenario[] { scUser, scProject, scTicketType, scStatus };
    }

}
