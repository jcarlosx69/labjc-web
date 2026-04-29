package es.labjc.labjcweb.repository;

import es.labjc.labjcweb.model.DownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {

    /**
     * Devuelve el total de descargas agrupadas por día y app.
     * Resultado: [appName, fecha, total_descargas]
     * Ordenado de más reciente a más antiguo.
     */
    @Query("""
        SELECT a.appName, CAST(d.downloadedAt AS date), COUNT(d)
        FROM DownloadLog d
        JOIN d.app a
        GROUP BY a.appName, CAST(d.downloadedAt AS date)
        ORDER BY CAST(d.downloadedAt AS date) DESC, a.appName ASC
    """)
    List<Object[]> findDailyDownloadStats();

    /**
     * Total histórico de descargas por app.
     * Resultado: [appName, total_descargas]
     */
    @Query("""
        SELECT a.appName, COUNT(d)
        FROM DownloadLog d
        JOIN d.app a
        GROUP BY a.appName
        ORDER BY COUNT(d) DESC
    """)
    List<Object[]> findTotalDownloadsByApp();
}