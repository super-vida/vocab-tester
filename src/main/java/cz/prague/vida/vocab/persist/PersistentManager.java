package cz.prague.vida.vocab.persist;

import static cz.prague.vida.vocab.VocabLogger.LOGGER;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import cz.prague.vida.vocab.entity.Lesson;
import cz.prague.vida.vocab.entity.LessonCheck;
import cz.prague.vida.vocab.entity.LessonGroup;
import cz.prague.vida.vocab.entity.Word;
import cz.prague.vida.vocab.entity.WordGroup;

/**
 * The Class PersistentManager.
 */
public class PersistentManager {

	private static final String PERSISTENCE_UNIT_NAME = "vocab";
	private static final String LESSON_ID = "lessonId";
	private static EntityManagerFactory factory;
	private EntityManager entityManager;

	private static PersistentManager instance = null;

	/**
	 * Instantiates a new persistent manager.
	 */
	private PersistentManager() {
		super();
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	/**
	 * Gets the single instance of PersistentManager.
	 *
	 * @return single instance of PersistentManager
	 */
	public static synchronized PersistentManager getInstance() {
		if (instance == null) {
			instance = new PersistentManager();
		}
		return instance;
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	private EntityManager getEntityManager() {
		if (entityManager == null) {
			return factory.createEntityManager();
		}
		return entityManager;
	}

	/**
	 * Persist.
	 *
	 * @param entity
	 *            the entity
	 */
	public void persist(Object entity) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while persist", e);
			em.getTransaction().rollback();

		}
		finally {
			em.close();
		}
	}

	/**
	 * Find lesson.
	 *
	 * @param lessonName
	 *            the lesson name
	 * @return the lesson
	 */
	public Lesson findLesson(String lessonName) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select l from Lesson l where l.name = :name");
			q.setParameter("name", lessonName);
			List<?> list = q.getResultList();
			if (list != null && list.size() == 1) {
				return (Lesson) list.get(0);
			}
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while findLesson", e);
		}
		finally {
			em.close();
		}
		return null;
	}

	/**
	 * Gets the distinct count.
	 *
	 * @return the distinct count
	 */
	public long getDistinctCount() {
		return processCount("select count(*) from Word w where w.language = 'en'");
	}

	/**
	 * Gets the lesson count.
	 *
	 * @return the lesson count
	 */
	public long getLessonCount() {
		return processCount("select count(*) from Lesson");
	}

	/**
	 * Gets the word total count.
	 *
	 * @return the word total count
	 */
	public long getWordTotalCount() {
		return processCount("select sum(totalCount) from Lesson l");
	}

	/**
	 * Gets the word total correct count.
	 *
	 * @return the word total correct count
	 */
	public long getWordTotalCorrectCount() {
		return processCount("select sum(correctCount) from Lesson l");
	}

	/**
	 * Process count.
	 *
	 * @param query
	 *            the query
	 * @return the long
	 */
	public long processCount(String query) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery(query);
			Long bd = (Long) q.getSingleResult();
			return bd.longValue();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while process count", e);
		}
		finally {
			em.close();
		}
		return 0;
	}

	/**
	 * Gets the all lessons.
	 *
	 * @return the all lessons
	 */
	@SuppressWarnings("unchecked")
	public List<Lesson> getAllLessons() {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select l from Lesson l order by name");
			return q.getResultList();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while get all lessons", e);
		}
		finally {
			em.close();
		}
		return new ArrayList<>();
	}

	/**
	 * Gets the lesson history.
	 *
	 * @param lesson
	 *            the lesson
	 * @return the lesson history
	 */
	@SuppressWarnings("unchecked")
	public List<LessonCheck> getLessonHistory(Lesson lesson) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select lc from LessonCheck lc where lessonId = :lessonId order by time desc");
			q.setParameter(LESSON_ID, lesson.getId());
			return q.getResultList();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while get lesson history", e);
		}
		finally {
			em.close();
		}
		return new ArrayList<>();
	}

	/**
	 * Gets the all lesson groups.
	 *
	 * @return the all lesson groups
	 */
	@SuppressWarnings("unchecked")
	public List<LessonGroup> getAllLessonGroups() {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select l from LessonGroup l order by name");
			return q.getResultList();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while get all lesson groups", e);
		}
		finally {
			em.close();
		}
		return new ArrayList<>();
	}

	/**
	 * Gets the config.
	 *
	 * @param name
	 *            the name
	 * @return the config
	 */
	public String getConfig(String name) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select c.value from Config c where c.name = :name");
			q.setParameter("name", name);
			List<?> list = q.getResultList();
			if (list != null && !list.isEmpty()) {
				return (String) list.get(0);
			}
		}
		finally {
			em.close();
		}
		return null;
	}

	/**
	 * Find word.
	 *
	 * @param text
	 *            the text
	 * @param language
	 *            the language
	 * @return the word
	 */
	public Word findWord(String text, String language) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select w from Word w where w.text = :text and w.language = :language");
			q.setParameter("text", text);
			q.setParameter("language", language);
			List<?> list = q.getResultList();
			if (list != null && !list.isEmpty()) {
				return (Word) list.get(0);
			}
		}
		finally {
			em.close();
		}
		return null;
	}

	/**
	 * Find word groups.
	 *
	 * @param lessonId
	 *            the lesson id
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<WordGroup> findWordGroups(long lessonId) {
		EntityManager em = getEntityManager();
		try {
			Query q = em.createQuery("select w from WordGroup w where w.lessonId = :lessonId");
			q.setParameter(LESSON_ID, lessonId);
			return q.getResultList();
		}
		finally {
			em.close();
		}
	}

	/**
	 * Gets the word.
	 *
	 * @param wordId
	 *            the word id
	 * @return the word
	 */
	public Word getWord(Long wordId) {
		EntityManager em = getEntityManager();
		try {
			return em.find(Word.class, wordId);
		}
		finally {
			em.close();
		}
	}

	/**
	 * Delete lesson.
	 *
	 * @param lesson
	 *            the lesson
	 */
	public void deleteLesson(Lesson lesson) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
//			Query q = em.createQuery("delete from WordGroup wg where wg.lessonId = :lessonId");
//			q.setParameter(LESSON_ID, lesson.getId());
//			q.executeUpdate();
			Query q = em.createQuery("delete from Lesson l where l.id = :lessonId");
			q.setParameter(LESSON_ID, lesson.getId());
			q.executeUpdate();
			em.getTransaction().commit();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while delete lesson", e);
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}
	}

	/**
	 * Delete word group.
	 *
	 * @param lessonId
	 *            the lesson id
	 * @param word1Id
	 *            the word 1 id
	 */
	public void deleteWordGroup(Long lessonId, Long word1Id) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			Query q = em.createQuery("delete from WordGroup wg where wg.lessonId = :lessonId and wg.word1Id = :word1Id");
			q.setParameter(LESSON_ID, lessonId);
			q.setParameter("word1Id", word1Id);
			q.executeUpdate();
			em.getTransaction().commit();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "Error while  delete word group", e);
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}
	}

	/**
	 * Update word correct count.
	 *
	 * @param id            the id
	 * @param date            the date
	 * @param correct the correct
	 */
	public void updateWordCount(Long id, Date date, boolean correct) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			Query q = em.createNamedQuery(correct ? Word.QUERY_UPDATE_CORRECT : Word.QUERY_UPDATE_INCORRECT);
			q.setParameter("id", id);
			q.setParameter("date", date);
			q.executeUpdate();
			em.getTransaction().commit();
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "error", e);
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}

	}
	
	public void updateOldLessons() {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			Query q = em.createNamedQuery(Lesson.QUERY_UPDATE_OLD_LESSONS);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -90);
			q.setParameter("time", cal.getTime());
			int updated = q.executeUpdate();
			em.getTransaction().commit();
			LOGGER.log(Level.INFO, "Updated old lessons :" + updated);
		}
		catch (PersistenceException e) {
			LOGGER.log(Level.SEVERE, "error", e);
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}

	}

	/**
	 * Close.
	 */
	public void close() {
		if (entityManager != null && entityManager.isOpen()) {
			LOGGER.info("Closing entity manager...");
			entityManager.close();
		}
		if (factory != null && factory.isOpen()) {
			LOGGER.info("Closing factory...");
			factory.close();
		}

	}

}
