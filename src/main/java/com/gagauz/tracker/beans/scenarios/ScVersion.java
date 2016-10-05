package com.gagauz.tracker.beans.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gagauz.tracker.beans.dao.FeatureDao;
import com.gagauz.tracker.beans.dao.FeatureVersionDao;
import com.gagauz.tracker.beans.dao.ProjectDao;
import com.gagauz.tracker.beans.dao.TicketCommentDao;
import com.gagauz.tracker.beans.dao.TicketDao;
import com.gagauz.tracker.beans.dao.TicketStatusDao;
import com.gagauz.tracker.beans.dao.TicketTypeDao;
import com.gagauz.tracker.beans.dao.UserDao;
import com.gagauz.tracker.beans.dao.VersionDao;
import com.gagauz.tracker.beans.dao.WorkLogDao;
import com.gagauz.tracker.beans.dao.WorkflowDao;
import com.gagauz.tracker.beans.setup.DataBaseScenario;
import com.gagauz.tracker.db.model.Attachment;
import com.gagauz.tracker.db.model.CvsRepo;
import com.gagauz.tracker.db.model.CvsType;
import com.gagauz.tracker.db.model.Feature;
import com.gagauz.tracker.db.model.FeatureVersion;
import com.gagauz.tracker.db.model.Project;
import com.gagauz.tracker.db.model.Ticket;
import com.gagauz.tracker.db.model.TicketStatus;
import com.gagauz.tracker.db.model.TicketType;
import com.gagauz.tracker.db.model.User;
import com.gagauz.tracker.db.model.Version;
import com.gagauz.tracker.db.model.WorkLog;
import com.gagauz.tracker.db.model.Workflow;
import com.gagauz.tracker.utils.RandomUtils;

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
	private WorkflowDao workflowDao;

	@Autowired
	private TicketCommentDao ticketCommentDao;

	@Autowired
	private ScUser scUser;

	private String[] fnames = { "Страница регистрации и авторизация", "Каталог товаров", "Главная страница",
			"Страница успешного оформления заказа",
			"Страница оформления заказа", "Личный кабинет - Персональные данные", "Личный кабинет - Адреса", "Личный кабинет - Заказы" };

	@Override
	protected void execute() {
		User user1 = this.userDao.findById(1);
		User user2 = this.userDao.findById(2);

		for (int i = 0; i < 1; i++) {
			Project project = new Project();
			project.setCode("TRACKER");
			project.setName("Трекер");
			this.projectDao.saveNoCommit(project);

			TicketType feature1 = create("Feature", project, null);
			create("Dev task", project, feature1);
			create("SA task", project, feature1);
			create("BA task", project, feature1);
			create("QA task", project, feature1);
			create("Bug", project, feature1);

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
				// repo.setBranch("trunk");
			}
			project.setCvsRepo(repo);
			this.projectDao.save(project);

			List<Feature> features = new ArrayList<>();

			for (int h = 0; h < 10; h++) {
				Feature feature = new Feature();
				feature.setProject(project);
				feature.setCreator(user1);
				feature.setName(RandomUtils.getRandomObject(this.fnames));
				feature.setDescription("Общее описание фичи, например, Авторизация на сайте для доступа к защищенным разделам.");
				features.add(feature);
			}
			this.featureDao.save(features);
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

				this.versionDao.save(version);
				for (Feature feature : features) {
					if (this.rand.nextBoolean() && this.rand.nextBoolean()) {
						continue;
					}

					FeatureVersion featureVersion = new FeatureVersion();
					featureVersion.setFeature(feature);
					featureVersion.setVersion(version);
					featureVersion.setCreator(user2);
					this.featureVersionDao.save(featureVersion);

					int stc = this.rand.nextInt(5) + 1;
					for (int k = 0; k < stc; k++) {
						Ticket ticket = new Ticket();
						ticket.setType(getRandomType());
						ticket.setFeatureVersion(featureVersion);
						ticket.setOwner(getRandomUser());
						ticket.setAuthor(getRandomUser());
						ticket.setSummary("Регистрация");
						ticket.setDescription("Описание того что нужно сделать конкретно, с макетами, если нужно.");
						int es = 15 * (this.rand.nextInt(10) + 1);
						ticket.setEstimate(es);
						WorkLog wl = null;
						ticket.setStatus(getRandomStatus());
						if (version.isReleased() || this.rand.nextBoolean()) {
							wl = new WorkLog();
							wl.setTicket(ticket);
							wl.setUser(ticket.getAuthor());
							if (!version.isReleased()) {
								wl.setLogTime(es / (this.rand.nextInt(5) + 1));
							} else {
								wl.setLogTime(es);
							}
						}
						if (this.rand.nextBoolean()) {
							Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
							Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
							ticket.setAttachments(Arrays.asList(a1, a2));
						}
						List<Workflow> cms = new ArrayList<>();
						if (this.rand.nextBoolean()) {
							for (int x = this.rand.nextInt(10) + 1; x > 0; x--) {
								Workflow cm = new Workflow();
								cm.setAuthor(getRandomUser());
								cm.setTicket(ticket);
								cm.setComment("Lorem ipsum dolorsit.");

								if (this.rand.nextBoolean()) {
									Attachment a1 = new Attachment("http://cs14114.vk.me/c622920/v622920701/10bf7/LDFJx3GuOic.jpg");
									Attachment a2 = new Attachment("https://pp.vk.me/c622419/v622419950/f5d8/wo6DQ2DE8s8.jpg");
									cm.setAttachments(Arrays.asList(a1, a2));
								}
								cms.add(cm);
							}
						}
						this.ticketDao.save(ticket);
						if (wl != null) {
							this.workLogDao.save(wl);
						}
						this.ticketDao.updateTicketProgessTime(ticket);
						if (!cms.isEmpty()) {
							this.workflowDao.save(cms);
						}

						int stc1 = this.rand.nextInt(3) + 1;
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
							this.ticketDao.save(bug);
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
		if (null == this.statusHash) {
			this.statusHash = new HashMap<>();
			for (TicketStatus s : this.statusDao.findAll()) {
				this.statusHash.put(s.getId(), s);
			}
		}
		return RandomUtils.getRandomObject(this.statusHash.values());
	}

	private TicketType getRandomType() {
		if (null == this.typeHash) {
			this.typeHash = new HashMap<>();
			for (TicketType s : this.typeDao.findAll()) {
				this.typeHash.put(s.getId(), s);
			}
		}
		return RandomUtils.getRandomObject(this.typeHash.values());
	}

	private User getRandomUser() {
		if (null == this.userHash) {
			this.userHash = new HashMap<>();
			for (User u : this.userDao.findAll()) {
				this.userHash.put(u.getId(), u);
			}
		}
		return RandomUtils.getRandomObject(this.userHash.values());
	}

	private TicketType create(String name, Project project, TicketType parent) {
		TicketType type = new TicketType();
		type.setName(name);
		type.setProject(project);
		type.setParent(parent);
		this.typeDao.save(type);

		if (null == this.typeHash) {
			this.typeHash = new HashMap<>();
		}
		this.typeHash.put(type.getId(), type);

		return type;
	}

	@Override
	protected DataBaseScenario[] getDependsOn() {
		return new DataBaseScenario[] { this.scUser };
	}

}
