/*
 * Copyright 2012 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.memory.set;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.procedure.primitive.IntProcedure;
import com.gs.collections.impl.MemoryTests;
import com.gs.collections.impl.list.primitive.IntInterval;
import com.gs.collections.impl.memory.MemoryTestBench;
import com.gs.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class ImmutableSetMemoryTest
{
    @Test
    @Category(MemoryTests.class)
    public void memoryForScaledImmutableSets()
    {
        IntProcedure procedure = new IntProcedure()
        {
            public void value(int size)
            {
                ImmutableSetMemoryTest.this.memoryForScaledSets(size);
            }
        };
        IntInterval.zeroTo(9).forEach(procedure);
        IntInterval.fromToBy(10, 100, 10).forEach(procedure);
    }

    public void memoryForScaledSets(int size)
    {
        MemoryTestBench.on(Set.class)
                .printContainerMemoryUsage("ImmutableSet", size, new SizedUnmodifiableHashSetFactory(size));
        MemoryTestBench.on(com.gs.collections.api.set.ImmutableSet.class)
                .printContainerMemoryUsage("ImmutableSet", size, new SizedImmutableGscSetFactory(size));
        MemoryTestBench.on(ImmutableSet.class)
                .printContainerMemoryUsage("ImmutableSet", size, new SizedImmutableGuavaSetFactory(size));
    }

    public static class SizedImmutableGscSetFactory implements Function0<com.gs.collections.api.set.ImmutableSet<Integer>>
    {
        private final int size;

        protected SizedImmutableGscSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public com.gs.collections.api.set.ImmutableSet<Integer> value()
        {
            UnifiedSet<Integer> set = UnifiedSet.newSet(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(Integer.valueOf(i));
            }
            return set.toImmutable();
        }
    }

    public static class SizedImmutableGuavaSetFactory implements Function0<ImmutableSet<Integer>>
    {
        private final int size;

        protected SizedImmutableGuavaSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public ImmutableSet<Integer> value()
        {
            ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
            for (int i = 0; i < this.size; i++)
            {
                builder.add(Integer.valueOf(i));
            }
            return builder.build();
        }
    }

    public static class SizedUnmodifiableHashSetFactory implements Function0<Set<Integer>>
    {
        private final int size;

        protected SizedUnmodifiableHashSetFactory(int size)
        {
            this.size = size;
        }

        @Override
        public Set<Integer> value()
        {
            if (this.size == 0)
            {
                return Collections.emptySet();
            }
            if (this.size == 1)
            {
                return Collections.singleton(Integer.valueOf(0));
            }
            HashSet<Integer> set = new HashSet<Integer>(this.size);
            for (int i = 0; i < this.size; i++)
            {
                set.add(Integer.valueOf(i));
            }
            return Collections.unmodifiableSet(set);
        }
    }
}
