/**
 * This file is part of Graylog Pipeline Processor.
 *
 * Graylog Pipeline Processor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog Pipeline Processor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog Pipeline Processor.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog.plugins.pipelineprocessor.functions;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.graylog.plugins.pipelineprocessor.ast.functions.Function;
import org.graylog.plugins.pipelineprocessor.functions.conversion.BooleanConversion;
import org.graylog.plugins.pipelineprocessor.functions.conversion.DoubleConversion;
import org.graylog.plugins.pipelineprocessor.functions.conversion.LongConversion;
import org.graylog.plugins.pipelineprocessor.functions.conversion.StringConversion;
import org.graylog.plugins.pipelineprocessor.functions.dates.FlexParseDate;
import org.graylog.plugins.pipelineprocessor.functions.dates.Now;
import org.graylog.plugins.pipelineprocessor.functions.dates.ParseDate;
import org.graylog.plugins.pipelineprocessor.functions.hashing.MD5;
import org.graylog.plugins.pipelineprocessor.functions.hashing.SHA1;
import org.graylog.plugins.pipelineprocessor.functions.hashing.SHA256;
import org.graylog.plugins.pipelineprocessor.functions.hashing.SHA512;
import org.graylog.plugins.pipelineprocessor.functions.json.JsonParse;
import org.graylog.plugins.pipelineprocessor.functions.json.SelectJsonPath;
import org.graylog.plugins.pipelineprocessor.functions.messages.CreateMessage;
import org.graylog.plugins.pipelineprocessor.functions.messages.DropMessage;
import org.graylog.plugins.pipelineprocessor.functions.messages.HasField;
import org.graylog.plugins.pipelineprocessor.functions.messages.RemoveField;
import org.graylog.plugins.pipelineprocessor.functions.messages.RouteToStream;
import org.graylog.plugins.pipelineprocessor.functions.messages.SetField;
import org.graylog.plugins.pipelineprocessor.functions.messages.SetFields;
import org.graylog.plugins.pipelineprocessor.functions.strings.Abbreviate;
import org.graylog.plugins.pipelineprocessor.functions.strings.Capitalize;
import org.graylog.plugins.pipelineprocessor.functions.strings.Contains;
import org.graylog.plugins.pipelineprocessor.functions.strings.Lowercase;
import org.graylog.plugins.pipelineprocessor.functions.strings.RegexMatch;
import org.graylog.plugins.pipelineprocessor.functions.strings.Substring;
import org.graylog.plugins.pipelineprocessor.functions.strings.Swapcase;
import org.graylog.plugins.pipelineprocessor.functions.strings.Uncapitalize;
import org.graylog.plugins.pipelineprocessor.functions.strings.Uppercase;
import org.graylog2.plugin.PluginModule;

public class ProcessorFunctionsModule extends PluginModule {
    @Override
    protected void configure() {
        // built-in functions
        addMessageProcessorFunction(BooleanConversion.NAME, BooleanConversion.class);
        addMessageProcessorFunction(DoubleConversion.NAME, DoubleConversion.class);
        addMessageProcessorFunction(LongConversion.NAME, LongConversion.class);
        addMessageProcessorFunction(StringConversion.NAME, StringConversion.class);

        // message related functions
        addMessageProcessorFunction(HasField.NAME, HasField.class);
        addMessageProcessorFunction(SetField.NAME, SetField.class);
        addMessageProcessorFunction(SetFields.NAME, SetFields.class);
        addMessageProcessorFunction(RemoveField.NAME, RemoveField.class);

        addMessageProcessorFunction(DropMessage.NAME, DropMessage.class);
        addMessageProcessorFunction(CreateMessage.NAME, CreateMessage.class);
        addMessageProcessorFunction(RouteToStream.NAME, RouteToStream.class);

        // input related functions
        addMessageProcessorFunction(FromInput.NAME, FromInput.class);

        // generic functions
        addMessageProcessorFunction(RegexMatch.NAME, RegexMatch.class);

        // string functions
        addMessageProcessorFunction(Abbreviate.NAME, Abbreviate.class);
        addMessageProcessorFunction(Capitalize.NAME, Capitalize.class);
        addMessageProcessorFunction(Contains.NAME, Contains.class);
        addMessageProcessorFunction(Lowercase.NAME, Lowercase.class);
        addMessageProcessorFunction(Substring.NAME, Substring.class);
        addMessageProcessorFunction(Swapcase.NAME, Swapcase.class);
        addMessageProcessorFunction(Uncapitalize.NAME, Uncapitalize.class);
        addMessageProcessorFunction(Uppercase.NAME, Uppercase.class);

        // json
        addMessageProcessorFunction(JsonParse.NAME, JsonParse.class);
        addMessageProcessorFunction(SelectJsonPath.NAME, SelectJsonPath.class);

        // dates
        addMessageProcessorFunction(Now.NAME, Now.class);
        addMessageProcessorFunction(ParseDate.NAME, ParseDate.class);
        addMessageProcessorFunction(FlexParseDate.NAME, FlexParseDate.class);

        // hash digest
        addMessageProcessorFunction(MD5.NAME, MD5.class);
        addMessageProcessorFunction(SHA1.NAME, SHA1.class);
        addMessageProcessorFunction(SHA256.NAME, SHA256.class);
        addMessageProcessorFunction(SHA512.NAME, SHA512.class);
    }

    protected void addMessageProcessorFunction(String name, Class<? extends Function<?>> functionClass) {
        addMessageProcessorFunction(binder(), name, functionClass);
    }

    public static MapBinder<String, Function<?>> processorFunctionBinder(Binder binder) {
        return MapBinder.newMapBinder(binder, TypeLiteral.get(String.class), new TypeLiteral<Function<?>>() {});
    }

    public static void addMessageProcessorFunction(Binder binder, String name, Class<? extends Function<?>> functionClass) {
        processorFunctionBinder(binder).addBinding(name).to(functionClass);

    }
}