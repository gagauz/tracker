package org.gagauz.tracker.web.utils;

import java.util.List;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import com.gagauz.tracker.services.dao.AbstractDao;
import com.xl0e.hibernate.dao.AbstractHibernateDao;
import com.xl0e.hibernate.utils.EntityFilter;

public class AbstractDaoGridDataSource<X> implements GridDataSource {

	private final Class<X> clazz;
	private final AbstractHibernateDao dao;
	private final EntityFilter<X> filter;
	private List<X> result;
	private int offset = 0;

	public AbstractDaoGridDataSource(EntityFilter<X> filter, Class<X> clazz) {
		this.filter = filter;
		this.clazz = clazz;
		this.dao = AbstractDao.getDao(clazz);
	}

	@Override
	public int getAvailableRows() {
		return dao.getCriteriaFilter().append(filter).count().intValue();
	}

	@Override
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
		offset = startIndex;
		filter.clearIndex().clearOrder().limit(startIndex, endIndex - startIndex + 1);
		sortConstraints.stream()
				.filter(sort -> sort.getColumnSort() == ColumnSort.ASCENDING)
				.forEach(sort -> filter.orderAsc(sort.getPropertyModel().getPropertyName()));

		sortConstraints.stream()
				.filter(sort -> sort.getColumnSort() == ColumnSort.DESCENDING)
				.forEach(sort -> filter.orderDecs(sort.getPropertyModel().getPropertyName()));

		result = dao.getCriteriaFilter().append(filter).list();
	}

	@Override
	public Object getRowValue(int index) {
		return result.get(index - offset);
	}

	@Override
	public Class<X> getRowType() {
		return clazz;
	}

}
