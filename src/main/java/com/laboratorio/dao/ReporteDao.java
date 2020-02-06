package com.laboratorio.dao;

import com.laboratorio.exception.ApiRequestException;
import com.laboratorio.model.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Slf4j
@Repository
public class ReporteDao {

    private static final String SQL_REPORT = "" +
            "SELECT TITULO " +
            ", NOMBRE_ARCHIVO " +
            ", DESCRIPCION " +
            ", ORIGEN_DE_DATOS " +
            ", FECHA_REGISTRA " +
            ", FECHA_ACTUALIZA " +
            "FROM dbo.REPORTES " +
            "WHERE ID = ?";

    private final RowMapper<Report> ROW_MAPPER_REPORT = (rs, i) -> {
        Report report = new Report();
        report.setTitle(rs.getString("TITULO"));
        report.setFileName(rs.getString("NOMBRE_ARCHIVO"));
        report.setDescription(rs.getString("DESCRIPCION"));
        report.setDataSource(rs.getString("ORIGEN_DE_DATOS"));
        return report;
    };

    final
    JdbcTemplate jdbcTemplate;

    public ReporteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Report getReport(Integer idReport) {
        try {
            return jdbcTemplate.queryForObject(SQL_REPORT, new Object[]{idReport}
            , new int[]{Types.INTEGER}, ROW_MAPPER_REPORT);
        } catch (Exception e) {
            log.error("An error has been occurred getting the report {}", e.getMessage());
            throw new ApiRequestException("An error has been occurred getting the report");
        }
    }

}
