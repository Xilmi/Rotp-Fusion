package rotp;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.Ships;
import rotp.model.game.GameSession;
import rotp.model.game.GameStatus;
import rotp.model.game.IGameOptions;
import rotp.model.game.MOO1GameOptions;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

public class RotpJSON {

    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule simpleModule = new SimpleModule("ROTP",
                new Version(1, 0, 0, null));
        simpleModule.addSerializer(new RotpJSON.GameSessionSerializer());
        simpleModule.addSerializer(new RotpJSON.ColorSerializer());
        simpleModule.addSerializer(new RotpJSON.ShipsSerializer());
        simpleModule.addSerializer(new RotpJSON.GameStatusSerializer());

        simpleModule.addDeserializer(java.awt.Color.class, new RotpJSON.ColorDeserializer());

        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(IGameOptions.class, MOO1GameOptions.class);
        simpleModule.setAbstractTypes(resolver);

        // TODO: Galaxy and others
        objectMapper.registerModule(simpleModule);
    }

    public static class GameSessionSerializer extends StdSerializer<GameSession> {
        protected GameSessionSerializer() {
            super(GameSession.class);
        }

        @Override
        public void serialize(GameSession gameSession, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("options", gameSession.options());
            jsonGenerator.writeObjectField("galaxy", gameSession.galaxy());
            jsonGenerator.writeObjectField("status", gameSession.status());
            jsonGenerator.writeNumberField("id", gameSession.id());

            jsonGenerator.writeObjectField("governorOptions", gameSession.getGovernorOptions());
            jsonGenerator.writeEndObject();
        }
    }

    /*
        public static class SerializableColor extends java.awt.Color {
            @JsonCreator
            public SerializableColor(@JsonProperty("red") int r, @JsonProperty("green") int g, @JsonProperty("blue") int b, @JsonProperty("alpha") int a) {
                super(r, g, b, a);
            }

            public SerializableColor(java.awt.Color c) {
                this(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            }
        }
        public static class SerializableColor2 {
            int r, g, b, a;
            @JsonCreator
            public SerializableColor2(@JsonProperty("red") int r, @JsonProperty("green") int g, @JsonProperty("blue") int b, @JsonProperty("alpha") int a) {
                this.r = r;
                this.g = g;
                this.b = b;
                this.a = a;
            }

            public SerializableColor2(java.awt.Color c) {
                this(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            }

            public int getR() {
                return r;
            }

            public int getG() {
                return g;
            }

            public int getB() {
                return b;
            }

            public int getA() {
                return a;
            }
        }
        public static List<SerializableColor> convert(List<java.awt.Color> colors) {
            if (colors == null) {
                return null;
            }
            List<SerializableColor> newList = new ArrayList<>(colors.size());
            for (java.awt.Color c: colors) {
                newList.add(new SerializableColor(c));
            }
            return newList;
        }
        public static List<SerializableColor2> convert2(List<java.awt.Color> colors) {
            if (colors == null) {
                return null;
            }
            List<SerializableColor2> newList = new ArrayList<>(colors.size());
            for (java.awt.Color c: colors) {
                newList.add(new SerializableColor2(c));
            }
            return newList;
        }
        public static class GameOptionsSerializer extends SerializerBase<MOO1GameOptions> {

            protected GameOptionsSerializer() {
                super(MOO1GameOptions.class);
            }

            @Override
            public void serialize(MOO1GameOptions options, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectField("opponentRaces", options.selectedOpponentRaces());//
                jsonGenerator.writeObjectField("colors", options.possibleColors());//
                jsonGenerator.writeObjectField("empireColors", convert2((List<Color>) getField("empireColors", options))); //
                jsonGenerator.writeObjectField("player", options.selectedPlayer());
                jsonGenerator.writeStringField("selectedGalaxySize", options.selectedGalaxySize());
                jsonGenerator.writeStringField("selectedGalaxyShape", options.selectedGalaxyShape());
                jsonGenerator.writeStringField("selectedGameDifficulty", options.selectedGameDifficulty());
                jsonGenerator.writeNumberField("selectedNumberOpponents", options.selectedNumberOpponents());
                jsonGenerator.writeBooleanField("communityAI", options.communityAI());
                jsonGenerator.writeBooleanField("disableRandomEvents", options.disableRandomEvents());
                jsonGenerator.writeEndObject();
            }
        }
    */
    public static class ColorSerializer extends StdSerializer<java.awt.Color> {
        protected ColorSerializer() {
            super(java.awt.Color.class);
        }

        @Override
        public void serialize(Color color, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("r", color.getRed());
            jsonGenerator.writeNumberField("g", color.getGreen());
            jsonGenerator.writeNumberField("b", color.getBlue());
            jsonGenerator.writeNumberField("a", color.getAlpha());
            jsonGenerator.writeEndObject();

        }
    }

    public static class ColorDeserializer extends StdDeserializer<Color> {

        protected ColorDeserializer() {
            super(java.awt.Color.class);
        }

        @Override
        public Color deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            int r = node.get("r").asInt();
            int g = node.get("g").asInt();
            int b = node.get("b").asInt();
            int a = node.get("a").asInt();
            java.awt.Color c = new java.awt.Color(r, g, b, a);
            return c;
        }
    }

    /*
        public static class GameOptionsDeserializer extends JsonDeserializer<IGameOptions> {

            @Override
            public IGameOptions deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

                MOO1GameOptions opt = new MOO1GameOptions();
                ObjectCodec oc = jp.getCodec();
                JsonNode node = oc.readTree(jp);

                opt.selectedNumberOpponents(node.get("selectedNumberOpponents").asInt());

                String[] opponentRaces = oc.treeToValue(node.get("opponentRaces"), String[].class);
                for (int i = 0; i < opponentRaces.length; i++) {
                    opt.selectedOpponentRace(i, opponentRaces[i]);
                }

                List<Integer> possibleColors = oc.treeToValue(node.get("colors"), List.class);
                opt.setColors(possibleColors);

                List<SerializableColor2> empireColors = oc.treeToValue(node.get("empireColors"), List.class);
                List<Color> empireColors2 = new ArrayList<>(empireColors.size());
                for (SerializableColor2 sc: empireColors) {
                    Color c = new Color(sc.r, sc.g, sc.b, sc.a);
                    empireColors2.add(c);
                }
                opt.setEmpireColors(empireColors2);



                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectField("opponentRaces", options.selectedOpponentRaces());
                jsonGenerator.writeObjectField("colors", options.possibleColors());
                jsonGenerator.writeObjectField("empireColors", convert2((List<Color>) getField("empireColors", options)));
                jsonGenerator.writeObjectField("player", options.selectedPlayer());
                jsonGenerator.writeStringField("selectedGalaxySize", options.selectedGalaxySize());
                jsonGenerator.writeStringField("selectedGalaxyShape", options.selectedGalaxyShape());
                jsonGenerator.writeStringField("selectedGameDifficulty", options.selectedGameDifficulty());
                jsonGenerator.writeNumberField("selectedNumberOpponents", options.selectedNumberOpponents());
                jsonGenerator.writeBooleanField("communityAI", options.communityAI());
                jsonGenerator.writeBooleanField("disableRandomEvents", options.disableRandomEvents());
                jsonGenerator.writeEndObject();
            }
        }
    */
    public static class GalaxySerializer extends StdSerializer<Galaxy> {
        protected GalaxySerializer() {
            super(Galaxy.class);
        }

        @Override
        public void serialize(Galaxy galaxy, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

//            jsonGenerator.writeStartObject();
//            jsonGenerator.writeObjectField("opponentRaces", options.selectedOpponentRaces());
//            jsonGenerator.writeObjectField("colors", options.possibleColors());
//            jsonGenerator.writeObjectField("empireColors", getField("empireColors", options));
//            jsonGenerator.writeObjectField("player", options.selectedPlayer());
//            jsonGenerator.writeStringField("selectedGalaxySize", options.selectedGalaxySize());
//            jsonGenerator.writeStringField("selectedGalaxyShape", options.selectedGalaxyShape());
//            jsonGenerator.writeStringField("selectedGameDifficulty", options.selectedGameDifficulty());
//            jsonGenerator.writeNumberField("selectedNumberOpponents", options.selectedNumberOpponents());
//            jsonGenerator.writeBooleanField("communityAI", options.communityAI());
//            jsonGenerator.writeBooleanField("disableRandomEvents", options.disableRandomEvents());
//            jsonGenerator.writeEndObject();
        }
    }

    public static class ShipsSerializer extends StdSerializer<Ships> {
        protected ShipsSerializer() {
            super(Ships.class);
        }

        @Override
        public void serialize(Ships ships, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("allFleets", getField("allFleets", ships));
            jsonGenerator.writeEndObject();
        }
    }

    public static class GameStatusSerializer extends StdSerializer<GameStatus> {
        protected GameStatusSerializer() {
            super(GameStatus.class);
        }

        @Override
        public void serialize(GameStatus gameStatus, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("status", getField("status", gameStatus));
            jsonGenerator.writeEndObject();
        }
    }

    // retrieve a field
    public static Object getStaticField(Class<?> cls, String field) {
        return getField(cls, field, null);
    }

    public static Object getField(String field, Object o) {
        return getField(o.getClass(), field, o);
    }

    public static Object getField(Class<?> cls, String field, Object o) {
        try {
            Field f = cls.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void setStaticField(Class<?> cls, String field, Object value) {
        setField(cls, field, null, value);
    }

    public static void setField(String field, Object object, Object value) {
        setField(object.getClass(), field, null, value);
    }

    public static void setField(Class<?> cls, String field, Object o, Object value) {
        try {
            Field f = cls.getDeclaredField(field);
            f.setAccessible(true);
            f.set(o, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
