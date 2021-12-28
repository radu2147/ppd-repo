package domain.validators;

import domain.Festival;

public class FestivalValidator implements Validator<Festival> {
    @Override
    public void validate(Festival entity) throws ValidationException {
        if(entity.getName().isEmpty() || entity.getGenre().isEmpty() || entity.getLocation().isEmpty())
            throw new ValidationException("Numele, genreul si locatia nu pot fi vide!");
        if(entity.getSeats()<=0)
            throw new ValidationException("Festivalul trebuie sa aiba locuri disponibile!");
    }
}
