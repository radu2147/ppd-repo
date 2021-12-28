package domain.validators;

import domain.Spectacol;

public class FestivalValidator implements Validator<Spectacol> {
    @Override
    public void validate(Spectacol entity) throws ValidationException {
        if(entity.getName().isEmpty())
            throw new ValidationException("Numele, genreul si locatia nu pot fi vide!");
        if(entity.getSold()<=0)
            throw new ValidationException("Festivalul trebuie sa aiba locuri disponibile!");
    }
}
