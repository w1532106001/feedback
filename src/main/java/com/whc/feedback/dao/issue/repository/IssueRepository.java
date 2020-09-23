package com.whc.feedback.dao.issue.repository;

import com.whc.feedback.dao.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: WHC
 * @Date: 2019/10/17 22:20
 * @description
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer>, JpaSpecificationExecutor<Issue> {
    @Query("select a.creator from Issue a group by a.creator")
    List<Integer> findAllGroupByCreator();

    @Query(value = "select distinct a.creator,b.realname FROM tblfeedback a LEFT JOIN zenoDB.dbo.tUser b on a.creator = b.user_id order by b.realname", nativeQuery = true)
    List<Object[]> getUserList();

    Issue findIssueById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update Issue set status = ?2 where id= ?1")
    Integer updateStatusById(Integer id, Byte status);

    @Transactional
    @Modifying
    @Query(value = "update Issue set status = ?2 , closeDate = ?3  where id= ?1")
    Integer updateStatusAndCloseDateById(Integer id, Byte status, Date date);

    @Transactional
    @Modifying
    @Query(value = "update Issue set status = ?2 , assignmentDate = ?3 , handlerId = ?4  where id= ?1")
    Integer updateStatusAndHandlerIdAndAssignmentDateById(Integer id, Byte status, Date date, Integer userId);

    @Query(value = "select count(id) from Issue where status = 1 and handlerId = ?1")
    int getNumByStatusAndHandlerId(Integer handlerId);

    @Transactional
    @Modifying
    @Query(value = "update Issue set isTemporarilyUnableToProcess = ?2  where id= ?1")
    Integer isTemporarilyUnableToProcess(Integer id, Boolean isTemporarilyUnableToProcess);

    @Query(value = "select a.user_id , count(a.user_id) as num  from listen a where CONVERT(varchar(8),a.create_time,112)=?1  GROUP BY a.user_id ORDER BY count(a.user_id) asc", nativeQuery = true)
    List<Object[]> getDailyListenCount(String time);

    @Query(value = "SELECT word,variations FROM  DictionaryDB.dbo.tWords WHERE word_id = ?1", nativeQuery = true)
    Map<String,String> getWordAndVariations(@Param("wordId") String wordId);

    @Query(value = "declare @alist varchar(Max)\n" +
            "set @alist = (select ''''+replace(listenlevel123, ' ',''',''')+'''' from DictionaryDB.dbo.tWordSimilarSoundandLook where word=?1)" +
            "select c.packageid, isnull(c.title_chinese, '') title_chinese,  isnull(c.title_english, '') title_english,  b.mediaid, a.scriptid, b.subpackageid, \n" +
            "                                    isnull(b.subtitle_chinese, '') subtitle_chinese, isnull(b.subtitle_english, '') subtitle_english, len(a.script_eng) cnt, a.script_eng, a.script_chn, a.blistening  from CivilizationDB.dbo.tblmediascripts a\n" +
            "                                    join CivilizationDB.dbo.tblmedias b on a.mediaid=b.mediaid\n" +
            "                                    join CivilizationDB.dbo.tblPackage c on c.packageid=b.packageid and c.status>0\n" +
            "         join CivilizationDB.dbo.tblPackageSubject d on d.subjectid=c.subjectid\n" +
            "                                    where \n" +
            "                                    a.blistening>0 \n" +
            "                                    and d.subjectid=200\n" +
            "                                    and a.script_eng like '% %'\n" +
            "                                    and  contains (a.script_eng, ?2  ) \n" +
            "and cast( a.scriptid as nvarchar(10)) not in (@alist)"+
            "                                    and a.scriptid not in (select script_id from QADB.dbo.listen where word_name=?1 )    \n" +
            "                                    order by cnt, a.script_eng", nativeQuery = true)
    List<Map<String,Object>> getScriptListByWordAndVariations(String word,String wordAndVariations);
}
