/*******************************************************************************
 * This file is part of Pebble.
 * 
 * Copyright (c) 2014 by Mitchell Bösecke
 * 
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble.node;

import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

import java.io.IOException;
import java.io.Writer;

public class ExtendsNode extends AbstractRenderableNode {

    Expression<?> parentExpression;

    public ExtendsNode(int lineNumber, Expression<?> parentExpression) {
        super(lineNumber);
        this.parentExpression = parentExpression;
    }

    @Override
    public void render(final PebbleTemplateImpl self, Writer writer, final EvaluationContext context)
            throws IOException {
        self.setParent(context, (String) parentExpression.evaluate(self, context));
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public Expression<?> getParentExpression() {
        return parentExpression;
    }
}
