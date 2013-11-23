package edu.umn.cs.recsys.ii;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.grouplens.lenskit.basic.AbstractGlobalItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

/**
 * Global item scorer to find similar items.
 * 
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleGlobalItemScorer extends AbstractGlobalItemScorer {
	private final SimpleItemItemModel model;

	@Inject
	public SimpleGlobalItemScorer(SimpleItemItemModel mod) {
		model = mod;
	}

	/**
	 * Score items with respect to a set of reference items.
	 * 
	 * @param items
	 *            The reference items.
	 * @param scores
	 *            The score vector. Its domain is the items to be scored, and the scores should be stored into this vector.
	 */
	@Override
	public void globalScore(@Nonnull Collection<Long> items, @Nonnull MutableSparseVector scores) {
		scores.fill(0);
		// each item's score is the sum of its similarity to each item in items, if they are
		// neighbors in the model.

		for (VectorEntry e : scores.fast(VectorEntry.State.EITHER)) {
			long item = e.getKey();
			List<ScoredId> neighbors = model.getNeighbors(item);
			double sumScore = 0;
			for (ScoredId thisNghbr : neighbors) {
				if (items.contains(thisNghbr.getId()))
					sumScore += thisNghbr.getScore();
			}
			scores.set(item, sumScore);
		}
	}
}
