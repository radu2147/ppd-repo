package objectProtocol;

import domain.FestivalDTO;

public class FestivalResponse implements Response{
    private Iterable<FestivalDTO> festivalDTOS;

    public FestivalResponse(Iterable<FestivalDTO> festivalDTOS) {
        this.festivalDTOS = festivalDTOS;
    }

    public Iterable<FestivalDTO> getFestivalDTOS() {
        return festivalDTOS;
    }
}
