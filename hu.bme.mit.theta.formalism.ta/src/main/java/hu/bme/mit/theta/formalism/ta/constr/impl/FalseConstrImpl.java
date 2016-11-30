package hu.bme.mit.theta.formalism.ta.constr.impl;

import static hu.bme.mit.theta.core.expr.impl.Exprs.False;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import hu.bme.mit.theta.core.expr.FalseExpr;
import hu.bme.mit.theta.formalism.common.decl.ClockDecl;
import hu.bme.mit.theta.formalism.ta.constr.FalseConstr;
import hu.bme.mit.theta.formalism.ta.utils.ClockConstrVisitor;

final class FalseConstrImpl implements FalseConstr {

	private static final int HASH_SEED = 242101;

	private static final String CC_LABEL = "false";

	@Override
	public Collection<? extends ClockDecl> getClocks() {
		return ImmutableSet.of();
	}

	@Override
	public FalseExpr toExpr() {
		return False();
	}

	@Override
	public <P, R> R accept(final ClockConstrVisitor<? super P, ? extends R> visitor, final P param) {
		return visitor.visit(this, param);
	}

	@Override
	public int hashCode() {
		return HASH_SEED;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof FalseConstr;
	}

	@Override
	public String toString() {
		return CC_LABEL;
	}
}
