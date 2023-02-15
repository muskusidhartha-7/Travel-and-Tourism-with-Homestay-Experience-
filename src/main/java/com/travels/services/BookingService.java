package com.travels.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import com.travels.models.Booking;
import com.travels.models.Payment;

@Service
public class BookingService {
	
	@Autowired NamedParameterJdbcTemplate temp;
	
	public void saveBooking(Booking b) {
		b.setStatus("Booked");
		final String sql="insert into bookings(tour,flight,hotel,insurance,bdate,userid,status) "
				+ "values(:tour,:flight,:hotel,:insurance,:bdate,:userid,:status)";
		temp.update(sql, getSqlParameterByModel(b));
	}
	
	public List<Booking> getMyBookings(String userid){
		Map<String,String> map=new HashMap<String, String>();
		map.put("userid", userid);
		return temp.query("select * from bookings where userid=:userid",map, new BookingRowMapper());
	}
	
	public List<Booking> getMyBookings(){
		return temp.query("select * from bookings order by bid desc",getSqlParameterByModel(null), new BookingRowMapper());
	}
	
	public void savePayment(Payment p) {
		final String sql="insert into payments(bid,amount,pmtdate,userid,mode) "
				+ "values(:bid,:amount,date(now()),:userid,:mode)";
		temp.update(sql, getPSqlParameterByModel(p));
	}
	
	public int getLastId() {
		return temp.queryForObject("select max(bid) from bookings", getSqlParameterByModel(null), Integer.class);
	}

	private SqlParameterSource getPSqlParameterByModel(Payment p) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        if (p != null) {
            ps.addValue("bid", p.getBid());
            ps.addValue("pmtdate", p.getPmtdate());
            ps.addValue("userid", p.getUserid());
            ps.addValue("mode", p.getMode());
            ps.addValue("amount", p.getAmount());
        }
        return ps;
    }
	
	private class PayRowMapper implements RowMapper<Payment>{

		@Override
		public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Payment pmt=new Payment();
			pmt.setBid(rs.getInt("bid"));
			pmt.setUserid(rs.getString("userid"));
			pmt.setMode(rs.getString("mode"));
			pmt.setPmtdate(rs.getString("pmtdate"));
			pmt.setAmount(rs.getInt("amount"));
			return pmt;
		}
		
	}
	
	private SqlParameterSource getSqlParameterByModel(Booking b) {
        MapSqlParameterSource ps = new MapSqlParameterSource();
        if (b != null) {
            ps.addValue("bid", b.getBid());
            ps.addValue("tour", b.getTour());
            ps.addValue("flight", b.getFlight());
            ps.addValue("hotel", b.getHotel());
            ps.addValue("insurance", b.getInsurance());
            ps.addValue("bdate", b.getBdate());
            ps.addValue("userid", b.getUserid());
            ps.addValue("status", b.getStatus());
        }
        return ps;
    }
	
	private class BookingRowMapper implements RowMapper<Booking>{

		@Override
		public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Booking user=new Booking();
			user.setBid(rs.getInt("bid"));
			user.setTour(rs.getString("tour"));
			user.setFlight(rs.getString("flight"));
			user.setHotel(rs.getString("hotel"));
			user.setUserid(rs.getString("userid"));
			user.setStatus(rs.getString("status"));
			user.setBdate(rs.getString("bdate"));
			user.setInsurance(rs.getString("insurance"));
			return user;
		}
		
	}
}
