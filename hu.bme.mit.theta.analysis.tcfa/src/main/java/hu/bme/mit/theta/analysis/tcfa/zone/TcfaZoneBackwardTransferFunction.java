package hu.bme.mit.theta.analysis.tcfa.zone;

import static hu.bme.mit.theta.formalism.ta.constr.impl.ClockConstrs.Eq;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import hu.bme.mit.theta.analysis.TransferFunction;
import hu.bme.mit.theta.analysis.tcfa.TcfaAction;
import hu.bme.mit.theta.analysis.zone.ZonePrecision;
import hu.bme.mit.theta.analysis.zone.ZoneState;
import hu.bme.mit.theta.formalism.common.decl.ClockDecl;
import hu.bme.mit.theta.formalism.ta.constr.ClockConstr;
import hu.bme.mit.theta.formalism.ta.op.ClockOp;
import hu.bme.mit.theta.formalism.ta.op.GuardOp;
import hu.bme.mit.theta.formalism.ta.op.ResetOp;

final class TcfaZoneBackwardTransferFunction implements TransferFunction<ZoneState, TcfaAction, ZonePrecision> {

	private static TcfaZoneBackwardTransferFunction INSTANCE = new TcfaZoneBackwardTransferFunction();

	private TcfaZoneBackwardTransferFunction() {
	}

	static TcfaZoneBackwardTransferFunction getInstance() {
		return INSTANCE;
	}

	@Override
	public Collection<ZoneState> getSuccStates(final ZoneState state, final TcfaAction action,
			final ZonePrecision precision) {
		final ZoneState succState = pre(state, action, precision);

		if (succState.isBottom()) {
			return ImmutableSet.of();
		} else {
			return ImmutableSet.of(succState);
		}
	}

	ZoneState pre(final ZoneState state, final TcfaAction action, final ZonePrecision precision) {
		final ZoneState.ZoneOperations prevStateBuilder = state.transform();

		for (final ClockOp op : Lists.reverse(action.getClockOps())) {
			if (op instanceof ResetOp) {
				final ResetOp resetOp = (ResetOp) op;
				final ClockDecl clock = resetOp.getClock();
				final int value = resetOp.getValue();
				prevStateBuilder.and(Eq(clock, value));
				prevStateBuilder.free(clock);

			} else if (op instanceof GuardOp) {
				prevStateBuilder.execute(op);

			} else {
				throw new AssertionError();
			}
		}

		for (final ClockConstr invar : action.getTargetClockInvars()) {
			prevStateBuilder.and(invar);
		}

		if (!action.getEdge().getSource().isUrgent()) {
			prevStateBuilder.down();
		}

		for (final ClockConstr invar : action.getSourceClockInvars()) {
			prevStateBuilder.and(invar);
		}

		return prevStateBuilder.done();
	}

}